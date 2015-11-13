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
package com.navercorp.pinpoint.plugin.sample._12_Asynchronous_Trace;

import java.security.ProtectionDomain;

import com.navercorp.pinpoint.bootstrap.async.AsyncTraceIdAccessor;
import com.navercorp.pinpoint.bootstrap.context.AsyncTraceId;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentMethod;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformCallback;
import com.navercorp.pinpoint.bootstrap.interceptor.SpanAsyncEventSimpleAroundInterceptor;
import com.navercorp.pinpoint.bootstrap.interceptor.scope.ExecutionPolicy;
import com.navercorp.pinpoint.bootstrap.interceptor.scope.InterceptorScope;
import com.navercorp.pinpoint.plugin.sample.SamplePluginConstants;

import static com.navercorp.pinpoint.common.util.VarArgs.va;

/**
 * To trace an async invocation you have to
 * 
 * 1. Intercept a method initiating an async task and issues a new {@link AsyncTraceId}.
 * 2. Pass the AsyncTraceId to the handler of the async task. 
 * 3. Add a field with {@link AsyncTraceIdAccessor} to the class handling the async task.   
 * 4. Intercept a method handling the async task with an interceptor extending {@link SpanAsyncEventSimpleAroundInterceptor}
 * 
 * 
 * In this sample, {@link AsyncInitiator} transforms TargetClass12_AsyncInitiator, which initiates async task as its name says.
 * {@link Worker} transforms TargetClass12_Worker, which handles async task initiated by TargetClass12_AsyncInitiator.
 */
public class Sample_12_Asynchronous_Trace {
    private static final String SCOPE_NAME = "AsyncSample";
    
    public static class AsyncInitiator implements TransformCallback {
        @Override
        public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
            // Scope interceptors to pass AsyncTraceId as interceptor scope invocation attachment.
            InterceptorScope scope = instrumentor.getInterceptorScope(SCOPE_NAME);

            InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);
            InstrumentMethod targetMethod = target.getDeclaredMethod("asyncHello", "java.lang.String");
            targetMethod.addScopedInterceptor("com.navercorp.pinpoint.plugin.sample._12_Asynchronous_Trace.AsyncInitiatorInterceptor", scope);

            return target.toBytecode();
        }
    }
    
    public static class Worker implements TransformCallback {

        @Override
        public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
            // Scope interceptors to pass AsyncTraceId as interceptor scope invocation attachment.
            InterceptorScope scope = instrumentor.getInterceptorScope(SCOPE_NAME);
            
            InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);
            target.addField("com.navercorp.pinpoint.bootstrap.async.AsyncTraceIdAccessor");
            
            InstrumentMethod constructor = target.getConstructor("java.lang.String", "com.navercorp.plugin.sample.target.TargetClass12_Future");
            constructor.addScopedInterceptor("com.navercorp.pinpoint.plugin.sample._12_Asynchronous_Trace.WorkerConstructorInterceptor", scope, ExecutionPolicy.INTERNAL);
            
            InstrumentMethod run = target.getDeclaredMethod("run");
            run.addInterceptor("com.navercorp.pinpoint.plugin.sample._12_Asynchronous_Trace.WorkerRunInterceptor");

            return target.toBytecode();
        }
    }
    
    public static class Future implements TransformCallback {

        @Override
        public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
            InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);
            
            InstrumentMethod get = target.getDeclaredMethod("get");
            get.addInterceptor("com.navercorp.pinpoint.bootstrap.interceptor.BasicMethodInterceptor", va(SamplePluginConstants.MY_SERVICE_TYPE));

            return target.toBytecode();
        }
    }
}
