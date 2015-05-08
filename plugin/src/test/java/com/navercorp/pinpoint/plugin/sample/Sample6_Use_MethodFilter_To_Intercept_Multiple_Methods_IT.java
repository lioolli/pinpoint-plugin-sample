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

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.navercorp.pinpoint.bootstrap.instrument.MethodFilter;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifierHolder;
import com.navercorp.pinpoint.plugin.sample.MyPlugin;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import com.navercorp.target.TargetClass6;

/**
 * Intercept public methods by using {@link MethodFilter}
 * 
 * @see MyPlugin#sample6_Use_MethodFilter_To_Intercept_Multiple_Methods(com.navercorp.pinpoint.bootstrap.plugin.ProfilerPluginContext)
 * @author Jongho Moon
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent("target/my-pinpoint-agent")
@Dependency({"com.navercorp.pinpoint:plugin-example-target:[1.0.0,)"})
public class Sample6_Use_MethodFilter_To_Intercept_Multiple_Methods_IT {

    @Test
    public void test() throws Exception {
        String name = "Pinpoint";

        TargetClass6 target = new TargetClass6();
        target.publicMethod();
        target.publicMethod(name);
        
        Method publicMethod1 = TargetClass6.class.getDeclaredMethod("publicMethod");
        Method publicMethod2 = TargetClass6.class.getDeclaredMethod("publicMethod", String.class);
        Method protectedMethod = TargetClass6.class.getDeclaredMethod("protectedMethod");
        Method privateMethod = TargetClass6.class.getDeclaredMethod("privateMethod");
        Method packagePrivateMethod = TargetClass6.class.getDeclaredMethod("packagePrivateMethod");
        
        protectedMethod.setAccessible(true);
        protectedMethod.invoke(target);
        
        privateMethod.setAccessible(true);
        privateMethod.invoke(target);
        
        packagePrivateMethod.setAccessible(true);
        packagePrivateMethod.invoke(target);
        
        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache(System.out);
        verifier.printBlocks(System.out);
        
        verifier.verifyApi("PluginExample", publicMethod1);
        verifier.verifyApi("PluginExample", publicMethod2);
        
        // no more traces
        verifier.verifyTraceBlockCount(0);
    }
}
