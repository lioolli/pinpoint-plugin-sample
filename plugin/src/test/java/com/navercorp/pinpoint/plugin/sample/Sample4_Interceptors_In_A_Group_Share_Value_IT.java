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

import static com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier.ExpectedAnnotation.*;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.navercorp.pinpoint.bootstrap.interceptor.group.InterceptorGroupInvocation;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier.BlockType;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifierHolder;
import com.navercorp.pinpoint.plugin.sample.MyPlugin;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import com.navercorp.target.TargetClass4;

/**
 *  We want to trace {@link TargetClass4#outerMethod(java.lang.String)} when it's argument starts with "FOO". 
 *  The method invokes {@link TargetClass4#innerMethod(java.lang.String)} and we want to record its return value but don't want to record it's invocation.
 *  
 *  We can do this by sharing data as attachment of {@link InterceptorGroupInvocation}.
 *  When an InterceptorGroupInvocation is activated, interceptors in the group can share an attachment object.
 *  InterceptorGroupInvocation is activated when before() of any of interceptors in the group is invoked, but after() of it is not invoked.
 *  
 * @see MyPlugin#sample4_Interceptors_In_A_Group_Share_Value(com.navercorp.pinpoint.bootstrap.plugin.ProfilerPluginContext)
 * @author Jongho Moon
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent("target/my-pinpoint-agent")
@Dependency({"com.navercorp.pinpoint:plugin-sample-target:[1.0.0,)", "log4j:log4j:1.2.17"})
public class Sample4_Interceptors_In_A_Group_Share_Value_IT {

    @Test
    public void test() throws Exception {
        String name = "FOOBAR";
        int length = name.length();

        TargetClass4 target = new TargetClass4();
        target.outerMethod(name);
        
        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache(System.out);
        verifier.printBlocks(System.out);
        
        Method targetMethod = TargetClass4.class.getMethod("outerMethod", String.class);
        verifier.verifyTraceBlock(BlockType.EVENT, "PluginExample", targetMethod, null, null, null, null, args(name)[0], annotation("MyValue", length));
        
        // no more traces
        verifier.verifyTraceBlockCount(0);
    }
}
