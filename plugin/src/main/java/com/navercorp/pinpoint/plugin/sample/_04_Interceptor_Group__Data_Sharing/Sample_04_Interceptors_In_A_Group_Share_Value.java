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
package com.navercorp.pinpoint.plugin.sample._04_Interceptor_Group__Data_Sharing;

import java.security.ProtectionDomain;

import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentMethod;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.PinpointClassFileTransformer;
import com.navercorp.pinpoint.bootstrap.interceptor.group.ExecutionPolicy;
import com.navercorp.pinpoint.bootstrap.interceptor.group.InterceptorGroup;

/*
 * Sometimes interceptors need to share values to trace a transaction.
 * You can use InterceptorGroup for that purpose.
 */
public class Sample_04_Interceptors_In_A_Group_Share_Value implements PinpointClassFileTransformer {

    @Override
    public byte[] transform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
        InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);
        
        // Get the group object from Instrumentor
        InterceptorGroup group = instrumentor.getInterceptorGroup("SAMPLE_04_GROUP");

        // Put interceptors need to share data to a same group.
        InstrumentMethod outerMethod = target.getDeclaredMethod("outerMethod", "java.lang.String");
        outerMethod.addGroupedInterceptor("com.navercorp.pinpoint.plugin.sample._04_Interceptor_Group__Data_Sharing.OuterMethodInterceptor", group);
        
        // Note that execution policy of InnerMethodInterceptor is INTERNAL to make the interceptor runs only when other interceptor in the group is active.
        InstrumentMethod innerMethod = target.getDeclaredMethod("innerMethod", "java.lang.String");
        innerMethod.addGroupedInterceptor("com.navercorp.pinpoint.plugin.sample._04_Interceptor_Group__Data_Sharing.InnerMethodInterceptor", group, ExecutionPolicy.INTERNAL);
        
        return target.toBytecode();
    }

}
