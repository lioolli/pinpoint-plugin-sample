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
package com.navercorp.pinpoint.plugin.sample._03_Interceptor_Group__Prevent_Duplicated_Trace;

import java.security.ProtectionDomain;

import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentMethod;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.PinpointClassFileTransformer;
import com.navercorp.pinpoint.bootstrap.interceptor.group.ExecutionPolicy;
import com.navercorp.pinpoint.bootstrap.interceptor.group.InterceptorGroup;
import com.navercorp.pinpoint.plugin.sample.SamplePluginConstants;

import static com.navercorp.pinpoint.common.util.VarArgs.va;

/**
 * We want to trace TargetClass03's methods targetMethodA, and targetMethodB which invokes targetMethodA.
 * In this case, if targetMethodB is executed, 2 traces are recorded.
 * To prevent such duplication, you can group interceptors.
 * 
 * Interceptors associated with a {@link InterceptorGroup} can specify the {@link ExecutionPolicy}.
 * 
 * - ALWAYS: execute the interceptor no matter other interceptors in the same group are active or not.
 * - BOUNDARY: execute the interceptor only if no other interceptors in the same group are active. 
 * - INTERNAL: execute the interceptor only if at least one interceptor in the same group is active.
 * 
 * An interceptor is active when it's before() method is executed but after() is not.
 * 
 * The default execution policy is BOUNDARY. 
 */
public class Sample_03_Use_Interceptor_Group_To_Prevent_Duplicated_Trace implements PinpointClassFileTransformer {

    @Override
    public byte[] transform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
        InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);
        
        // Get the group object from Instrumentor
        InterceptorGroup group = instrumentor.getInterceptorGroup("SAMPLE_GROUP");

        // Add interceptor with a group and an constructor argument
        InstrumentMethod targetMethodA = target.getDeclaredMethod("targetMethodA");
        targetMethodA.addGroupedInterceptor("com.navercorp.pinpoint.bootstrap.interceptor.BasicMethodInterceptor", va(SamplePluginConstants.MY_SERVICE_TYPE), group);
        
        // Add interceptor with a group and an constructor argument
        InstrumentMethod targetMethodB = target.getDeclaredMethod("targetMethodB", "int");
        targetMethodB.addGroupedInterceptor("com.navercorp.pinpoint.bootstrap.interceptor.BasicMethodInterceptor", va(SamplePluginConstants.MY_SERVICE_TYPE), group);
        
        return target.toBytecode();
    }
}
