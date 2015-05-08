/**
 * Copyright 2014 NAVER Corp.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.navercorp.pinpoint.plugin.sample;

import static com.navercorp.pinpoint.common.trace.HistogramSchema.*;

import java.lang.instrument.ClassFileTransformer;

import com.navercorp.pinpoint.bootstrap.interceptor.group.ExecutionPolicy;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPlugin;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPluginContext;
import com.navercorp.pinpoint.bootstrap.plugin.transformer.ClassFileTransformerBuilder;
import com.navercorp.pinpoint.common.trace.AnnotationKey;
import com.navercorp.pinpoint.common.trace.ServiceType;

/**
 * @author Jongho Moon
 *
 */
public class MyPlugin implements ProfilerPlugin {
    public static final ServiceType MY_SERVICE_TYPE = ServiceType.of(5099, "PluginExample", NORMAL_SCHEMA);
    
    public static final AnnotationKey ANNOTATION_KEY_MY_VALUE = new AnnotationKey(9998, "MyValue");
    public static final AnnotationKey ANNOTATION_KEY_RETURN_VALUE = new AnnotationKey(9999, "ReturnValue");

    @Override
    public void setup(ProfilerPluginContext context) {
        sample1_Inject_BasicMethodInterceptor(context);
        sample2_Inject_Custom_Interceptor(context);
        sample3_Use_Interceptor_Group_To_Prevent_Redundant_Trace(context);
        sample4_Interceptors_In_A_Group_Share_Value(context);
        sample5_Intercept_Constructor(context);
        sample6_Use_MethodFilter_To_Intercept_Multiple_Methods(context);
        sample7_Access_To_Fields(context);
        sample8_Inject_Metadata(context);
    }
    
    /*
     * Pinpiont provides BasicMethodInterceptor which records method invocation time and exception.
     * This example shows how to inject it to a target method. It also shows how to pass constructor arguments to interceptor.
     */
    private void sample1_Inject_BasicMethodInterceptor(ProfilerPluginContext context) {
        ClassFileTransformerBuilder builder = context.getClassFileTransformerBuilder("com.navercorp.target.TargetClass1");
        builder.editMethod("targetMethod", "java.lang.String").injectInterceptor("com.navercorp.pinpoint.bootstrap.interceptor.BasicMethodInterceptor", MY_SERVICE_TYPE);
        
        ClassFileTransformer transformer = builder.build();
        context.addClassFileTransformer(transformer);
    }

    /*
     *  You can inject a custom interceptor too.
     */
    private void sample2_Inject_Custom_Interceptor(ProfilerPluginContext context) {
        ClassFileTransformerBuilder builder = context.getClassFileTransformerBuilder("com.navercorp.target.TargetClass2");
        builder.editMethod("targetMethod", "java.lang.String").injectInterceptor("com.navercorp.pinpoint.plugin.sample.interceptor.Sample2_RecordArgsAndReturnValueInterceptor");
        
        ClassFileTransformer transformer = builder.build();
        context.addClassFileTransformer(transformer);
    }

    private void sample3_Use_Interceptor_Group_To_Prevent_Redundant_Trace(ProfilerPluginContext context) {
        ClassFileTransformerBuilder builder = context.getClassFileTransformerBuilder("com.navercorp.target.TargetClass3");
        builder.editMethod("targetMethodA").injectInterceptor("com.navercorp.pinpoint.bootstrap.interceptor.BasicMethodInterceptor", MY_SERVICE_TYPE).group("EXAMPLE_GROUP");
        builder.editMethod("targetMethodB", "int").injectInterceptor("com.navercorp.pinpoint.bootstrap.interceptor.BasicMethodInterceptor", MY_SERVICE_TYPE).group("EXAMPLE_GROUP");
        
        ClassFileTransformer transformer = builder.build();
        context.addClassFileTransformer(transformer);
    }

    private void sample4_Interceptors_In_A_Group_Share_Value(ProfilerPluginContext context) {
        ClassFileTransformerBuilder builder = context.getClassFileTransformerBuilder("com.navercorp.target.TargetClass4");
        
        builder.editMethod("outerMethod", "java.lang.String").injectInterceptor("com.navercorp.pinpoint.plugin.sample.interceptor.Sample4_OuterMethodInterceptor").group("EXAMPLE_GROUP");
        builder.editMethod("innerMethod", "java.lang.String").injectInterceptor("com.navercorp.pinpoint.plugin.sample.interceptor.Sample4_InnerMethodInterceptor").group("EXAMPLE_GROUP", ExecutionPolicy.INTERNAL);;
        
        ClassFileTransformer transformer = builder.build();
        context.addClassFileTransformer(transformer);
    }
    
    private void sample5_Intercept_Constructor(ProfilerPluginContext context) {
        ClassFileTransformerBuilder builder = context.getClassFileTransformerBuilder("com.navercorp.target.TargetClass5");
        
        builder.editConstructor("java.lang.String").injectInterceptor("com.navercorp.pinpoint.plugin.sample.interceptor.Sample5_RecordArgsInterceptor");
        
        ClassFileTransformer transformer = builder.build();
        context.addClassFileTransformer(transformer);
    }

    private void sample6_Use_MethodFilter_To_Intercept_Multiple_Methods(ProfilerPluginContext context) {
        ClassFileTransformerBuilder builder = context.getClassFileTransformerBuilder("com.navercorp.target.TargetClass6");
        
        builder.editMethods(new MyMethodFilter()).injectInterceptor("com.navercorp.pinpoint.bootstrap.interceptor.BasicMethodInterceptor", MY_SERVICE_TYPE);
        
        ClassFileTransformer transformer = builder.build();
        context.addClassFileTransformer(transformer);
    }
    
    private void sample7_Access_To_Fields(ProfilerPluginContext context) {
        ClassFileTransformerBuilder builder = context.getClassFileTransformerBuilder("com.navercorp.target.TargetClass7");
        
        builder.injectFieldAccessor("hiddenField");
        builder.editMethod("targetMethod").injectInterceptor("com.navercorp.pinpoint.plugin.sample.interceptor.Sample7_RecordFieldInterceptor");
        
        ClassFileTransformer transformer = builder.build();
        context.addClassFileTransformer(transformer);
    }

    private void sample8_Inject_Metadata(ProfilerPluginContext context) {
        ClassFileTransformerBuilder producerTransformerBuilder = context.getClassFileTransformerBuilder("com.navercorp.target.TargetClass8_Producer");
        producerTransformerBuilder.editMethod("produce").injectInterceptor("com.navercorp.pinpoint.plugin.sample.interceptor.Sample8_ProducerInterceptor");
        context.addClassFileTransformer(producerTransformerBuilder.build());
        
        ClassFileTransformerBuilder messageTransformerBuilder = context.getClassFileTransformerBuilder("com.navercorp.target.TargetClass8_Message");
        messageTransformerBuilder.injectMetadata("producerName");
        context.addClassFileTransformer(messageTransformerBuilder.build());
        
        ClassFileTransformerBuilder consumerTransformerBuilder = context.getClassFileTransformerBuilder("com.navercorp.target.TargetClass8_Consumer");
        consumerTransformerBuilder.editMethod("consume", "com.navercorp.target.TargetClass8_Message").injectInterceptor("com.navercorp.pinpoint.plugin.sample.interceptor.Sample8_ConsumerInterceptor");
        context.addClassFileTransformer(consumerTransformerBuilder.build());
    }
}
