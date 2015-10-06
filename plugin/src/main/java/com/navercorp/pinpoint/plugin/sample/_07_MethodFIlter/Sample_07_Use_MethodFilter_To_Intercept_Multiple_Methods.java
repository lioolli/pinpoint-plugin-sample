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
package com.navercorp.pinpoint.plugin.sample._07_MethodFIlter;

import java.security.ProtectionDomain;

import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentMethod;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.MethodFilter;
import com.navercorp.pinpoint.bootstrap.instrument.MethodFilters;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.PinpointClassFileTransformer;
import com.navercorp.pinpoint.plugin.sample.SamplePluginConstants;

import static com.navercorp.pinpoint.common.util.VarArgs.va;

/**
 * If you want to add same interceptor to many methods, use {@link MethodFilter}.
 * 
 * {@link MethodFilters} provides factory methods for pre-defined filters.
 */
public class Sample_07_Use_MethodFilter_To_Intercept_Multiple_Methods implements PinpointClassFileTransformer {

    @Override
    public byte[] transform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
        InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);

        // Get target methods filtered by name.
        for (InstrumentMethod method : target.getDeclaredMethods(MethodFilters.name("recordMe"))) {
            // Add interceptor to each method. Note that every method will be injected a dedicated interceptor instance. 
            method.addInterceptor("com.navercorp.pinpoint.bootstrap.interceptor.BasicMethodInterceptor", va(SamplePluginConstants.MY_SERVICE_TYPE));
        }
        
        // To make methods share an interceptor object, use addInterceptor(int) like this.
        int interceptorId = -1;
        for (InstrumentMethod method : target.getDeclaredMethods(MethodFilters.name("logMe"))) {
            if (interceptorId == -1) {
                interceptorId = method.addInterceptor("com.navercorp.pinpoint.bootstrap.interceptor.LoggingInterceptor", va("SMAPLE_07_LOGGER"));
            } else {
                method.addInterceptor(interceptorId);
            }
        }
        
        return target.toBytecode();
    }
}
