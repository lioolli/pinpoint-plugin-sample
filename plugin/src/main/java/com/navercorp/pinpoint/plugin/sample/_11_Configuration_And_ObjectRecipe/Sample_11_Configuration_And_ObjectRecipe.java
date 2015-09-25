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
package com.navercorp.pinpoint.plugin.sample._11_Configuration_And_ObjectRecipe;

import java.security.ProtectionDomain;

import com.navercorp.pinpoint.bootstrap.config.ProfilerConfig;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentException;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentMethod;
import com.navercorp.pinpoint.bootstrap.instrument.Instrumentor;
import com.navercorp.pinpoint.bootstrap.instrument.transformer.PinpointClassFileTransformer;
import com.navercorp.pinpoint.bootstrap.plugin.ObjectRecipe;

/**
 * ProfilerPlugin and PinpointClassFileTransformer implementation classes are loaded by a plugin class loader whose parent is system class loader when Pinpoint agent is initialized. 
 * But interceptor classes are loaded by the class loader which loads the target class. These target class loaders cannot see the plugin class loader.
 * 
 * Therefore for a class X in a plugin, X in ProfilerPlugin and PinpointClassFileTransformer implementations is not identical with X in interceptors. 
 * This makes it impossible for a transformer to pass a constructor argument whose type is defined in the plugin to a interceptor. 
 * To handle this problem, you can pass an {@link ObjectRecipe} which describes how to create the argument.
 * 
 * Note that, for the same reason, you should avoid sharing values by static variables of classes defined in a plugin.
 * 
 * 
 * This sample also shows how to read configurations in pinpoint.config file via {@link ProfilerConfig}.
 */
public class Sample_11_Configuration_And_ObjectRecipe implements PinpointClassFileTransformer {

    @Override
    public byte[] transform(Instrumentor instrumentor, ClassLoader classLoader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws InstrumentException {
        InstrumentClass target = instrumentor.getInstrumentClass(classLoader, className, classfileBuffer);
        InstrumentMethod targetMethod = target.getDeclaredMethod("hello", "java.lang.String");
        
        // Get ProfilerConfig
        ProfilerConfig config = instrumentor.getTraceContext().getProfilerConfig();
        int maxLen = config.readInt("profiler.sample11.maxLen", 8);
        
        // If you pass StringTrimmer object directly like below, Pinpoint agent fails to create the interceptor instance. 
        // targetMethod.addInterceptor("com.navercorp.pinpoint.plugin.sample._11_Configuration_And_ObjectRecipe.HelloInterceptor", new StringTrimmer(maxLen));
        
        ObjectRecipe trimmerRecipe = ObjectRecipe.byConstructor("com.navercorp.pinpoint.plugin.sample._11_Configuration_And_ObjectRecipe.StringTrimmer", maxLen);
        targetMethod.addInterceptor("com.navercorp.pinpoint.plugin.sample._11_Configuration_And_ObjectRecipe.HelloInterceptor", trimmerRecipe);

        return target.toBytecode();
    }
}
