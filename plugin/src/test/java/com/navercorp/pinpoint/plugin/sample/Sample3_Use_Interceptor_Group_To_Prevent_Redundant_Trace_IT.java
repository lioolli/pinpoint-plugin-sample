/**
 * Copyright 2014 https://github.com/lioolli/pinpoint-plugin-samplehttps://github.com/lioolli/pinpoint-plugin-sampleNAVER Corp.
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

import com.navercorp.pinpoint.bootstrap.plugin.test.Expectations;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifierHolder;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import com.navercorp.target.TargetClass3;

/**
 * Both {@link TargetClass3#targetMethodA()} and {@link TargetClass3#targetMethodB(int)} have been injected interceptors.
 * But those interceptors are in same interceptor group and their execution policy is BOUNDARY which means "execute only when no other interceptor in the same group is active".
 * (interceptor is active when it's before() is invoked but not after() yet)
 * 
 * So in {@link #testA()}, only {@link TargetClass3#tergetMethodA()} is recorded and in {@link #testB()}, only {@link TargetClass3#targetMethodB(int)} is recorded.
 * 
 * @see MyPlugin#sample3_Use_Interceptor_Group_To_Prevent_Redundant_Trace(com.navercorp.pinpoint.bootstrap.plugin.ProfilerPluginContext)
 * @author Jongho Moon
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent("target/my-pinpoint-agent")
@Dependency({"com.navercorp.pinpoint:plugin-sample-target:[1.0.0,)", "log4j:log4j:1.2.17"})
public class Sample3_Use_Interceptor_Group_To_Prevent_Redundant_Trace_IT {

    @Test
    public void testA() throws Exception {
        TargetClass3 target = new TargetClass3();
        target.targetMethodA();
        
        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();
        
        Method targetMethod = TargetClass3.class.getMethod("targetMethodA");
        verifier.verifyTrace(Expectations.event("PluginExample", targetMethod));
        
        // no more traces
        verifier.verifyTraceCount(0);
    }
    
    @Test
    public void testB() throws Exception {
        TargetClass3 target = new TargetClass3();
        target.targetMethodB(3);
        
        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();
        
        Method targetMethod = TargetClass3.class.getMethod("targetMethodB", int.class);
        verifier.verifyTrace(Expectations.event("PluginExample", targetMethod));
        
        // no more traces
        verifier.verifyTraceCount(0);
    }
}
