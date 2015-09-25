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
package com.navercorp.pinpoint.plugin.sample._06_Constructor_Interceptor_Group_Limitation;

import java.security.ProtectionDomain;

import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentMethod;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.PinpointClassFileTransformer;
import com.navercorp.pinpoint.bootstrap.interceptor.group.InterceptorGroup;
import com.navercorp.pinpoint.plugin.sample.SamplePluginConstants;

/**
 * Constructor interceptors can be grouped too. But there is a limitation.
 * 
 * Java enforces that the first operation of a constructor must be invocation of a super constructor or an overloaded constructor.
 * So any injected code (including interceptor call) must come after this.
 * Therefore interceptor invocation order is different with normal method's one.
 * 
 * If an intercepted method A calls an intercepted method B, interceptors are invoked in this order
 * (A' is interceptor of A, B' is interceptor of B):
 *  
 * A'.before(), B'.before(), B'.after(), A'.before()
 *  
 * 
 * But if A and B are constructors, interceptors are executed like below:
 * B'.before(), B'.after(), A'.before(), A'.after()
 * 
 * 
 * This also effects ExecutionPolicy of grouped interceptors.
 * In case of methods, if A and B are in the same group with execution policy BOUNDARY, methods of B' is not executed.
 * 
 * But for constructors, methods of A' and B' are all executed because when B' is executed, A' is not active.
 */
public class Sample_06_Constructor_Interceptor_Group_Limitation implements PinpointClassFileTransformer {

    @Override
    public byte[] transform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
        InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);
        InterceptorGroup group = instrumentor.getInterceptorGroup("SAMPLE_GROUP");

        InstrumentMethod targetConstructorA = target.getConstructor();
        targetConstructorA.addGroupedInterceptor("com.navercorp.pinpoint.bootstrap.interceptor.BasicMethodInterceptor", group, SamplePluginConstants.MY_SERVICE_TYPE);
        
        InstrumentMethod targetConstructorB = target.getConstructor("int");
        targetConstructorB.addGroupedInterceptor("com.navercorp.pinpoint.bootstrap.interceptor.BasicMethodInterceptor", group, SamplePluginConstants.MY_SERVICE_TYPE);
        
        return target.toBytecode();
    }

}
