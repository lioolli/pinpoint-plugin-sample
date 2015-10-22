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
package com.navercorp.pinpoint.plugin.sample._08_Interceptor_Annotations;

import java.security.ProtectionDomain;

import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.TransformCallback;
import com.navercorp.pinpoint.bootstrap.interceptor.annotation.Group;
import com.navercorp.pinpoint.bootstrap.interceptor.annotation.TargetConstructor;
import com.navercorp.pinpoint.bootstrap.interceptor.annotation.TargetConstructors;
import com.navercorp.pinpoint.bootstrap.interceptor.annotation.TargetFilter;
import com.navercorp.pinpoint.bootstrap.interceptor.annotation.TargetMethod;
import com.navercorp.pinpoint.bootstrap.interceptor.annotation.TargetMethods;

/**
 * There are annotations for interceptor class to declare 1. targets of the interceptor, 2. interceptor group the interceptor is in.
 * 
 * To specify target, use {@link TargetMethod}, {@link TargetConstructor}, {@link TargetFilter}.
 * To specify multiple targets, use {@link TargetMethods}, {@link TargetConstructors}.
 * Note that these are effective only when the interceptor is injected by one of {@link InstrumentClass}'s addInterceptor() or addGroupedInterceptor() method.
 * 
 * To specify interceptor group, use {@link Group}.
 * If you pass other InterceptorGroup through addGroupedInterceptor(), the annotation will be ignored.
 * 
 * @see AnnotatedInterceptor
 */
public class Sample_08_Interceptor_Annotations implements TransformCallback {

    @Override
    public byte[] doInTransform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
        InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);
        
        target.addInterceptor("com.navercorp.pinpoint.plugin.sample._08_Interceptor_Annotations.AnnotatedInterceptor");

        return target.toBytecode();
    }
}
