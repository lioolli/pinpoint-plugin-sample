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

import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPlugin;
import com.navercorp.pinpoint.bootstrap.plugin.ProfilerPluginSetupContext;
import com.navercorp.pinpoint.plugin.sample._01_Injecting_BasicMethodInterceptor.Sample_01_Inject_BasicMethodInterceptor;
import com.navercorp.pinpoint.plugin.sample._02_Injecting_Custom_Interceptor.Sample_02_Inject_Custom_Interceptor;
import com.navercorp.pinpoint.plugin.sample._03_Interceptor_Group__Prevent_Duplicated_Trace.Sample_03_Use_Interceptor_Group_To_Prevent_Duplicated_Trace;
import com.navercorp.pinpoint.plugin.sample._04_Interceptor_Group__Data_Sharing.Sample_04_Interceptors_In_A_Group_Share_Value;
import com.navercorp.pinpoint.plugin.sample._05_Constructor_Interceptor.Sample_05_Constructor_Interceptor;
import com.navercorp.pinpoint.plugin.sample._06_Constructor_Interceptor_Group_Limitation.Sample_06_Constructor_Interceptor_Group_Limitation;
import com.navercorp.pinpoint.plugin.sample._07_MethodFIlter.Sample_07_Use_MethodFilter_To_Intercept_Multiple_Methods;
import com.navercorp.pinpoint.plugin.sample._08_Interceptor_Annotations.Sample_08_Interceptor_Annotations;
import com.navercorp.pinpoint.plugin.sample._09_Adding_Getter.Sample_09_Adding_Getter;
import com.navercorp.pinpoint.plugin.sample._10_Adding_Field.Sample_10_Adding_Field;
import com.navercorp.pinpoint.plugin.sample._11_Configuration_And_ObjectRecipe.Sample_11_Configuration_And_ObjectRecipe;
import com.navercorp.pinpoint.plugin.sample._12_Asynchronous_Trace.Sample_12_Asynchronous_Trace;
import com.navercorp.pinpoint.plugin.sample._13_RPC_Client.Sample_13_RPC_Client;

/**
 * Any Pinpoint profiler plugin must implement ProfilerPlugin interface.
 * ProfilerPlugin declares only one method {@link #setup(ProfilerPluginSetupContext)}.
 * You should implement the method to do whatever you need to setup your plugin with the passed ProfilerPluginSetupContext object.
 * 
 * @author Jongho Moon
 */
public class SamplePlugin implements ProfilerPlugin {
    
    @Override
    public void setup(ProfilerPluginSetupContext context) {
        addApplicationTypeDetector(context);
        addTransformers(context);
    }

    private void addTransformers(ProfilerPluginSetupContext context) {
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass01", new Sample_01_Inject_BasicMethodInterceptor());
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass02", new Sample_02_Inject_Custom_Interceptor());
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass03", new Sample_03_Use_Interceptor_Group_To_Prevent_Duplicated_Trace());
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass04", new Sample_04_Interceptors_In_A_Group_Share_Value());
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass05", new Sample_05_Constructor_Interceptor());
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass06", new Sample_06_Constructor_Interceptor_Group_Limitation());
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass07", new Sample_07_Use_MethodFilter_To_Intercept_Multiple_Methods());
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass08", new Sample_08_Interceptor_Annotations());
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass09", new Sample_09_Adding_Getter());
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass10_Producer", new Sample_10_Adding_Field.Producer());
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass10_Consumer", new Sample_10_Adding_Field.Consumer());
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass10_Message", new Sample_10_Adding_Field.Message());
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass11", new Sample_11_Configuration_And_ObjectRecipe());
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass12_AsyncInitiator", new Sample_12_Asynchronous_Trace.AsyncInitiator());
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass12_Future", new Sample_12_Asynchronous_Trace.Future());
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass12_Worker", new Sample_12_Asynchronous_Trace.Worker());
        context.addClassFileTransformer("com.navercorp.plugin.sample.target.TargetClass13_Client", new Sample_13_RPC_Client());
        
        // TODO root span
        // TODO client span
        // TODO weave
    }
    
    private void addApplicationTypeDetector(ProfilerPluginSetupContext context) {
        // TODO
    }
    
}
