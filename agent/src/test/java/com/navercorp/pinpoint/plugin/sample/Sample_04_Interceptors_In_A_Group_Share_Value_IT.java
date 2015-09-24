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

import static com.navercorp.pinpoint.bootstrap.plugin.test.Expectations.*;

import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.navercorp.pinpoint.bootstrap.interceptor.group.InterceptorGroupInvocation;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifierHolder;
import com.navercorp.pinpoint.plugin.sample._04_Interceptor_Group__Data_Sharing.Sample_04_Interceptors_In_A_Group_Share_Value;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import com.navercorp.plugin.sample.target.TargetClass04;

/**
 *  We want to trace {@link TargetClass04#outerMethod(java.lang.String)} when it's argument starts with "FOO". 
 *  In addition, we want to record the return value of {@link TargetClass04#innerMethod(java.lang.String)} which is invoked by outerMethod() but don't want to record invocation of innerMethod().
 *  
 *  We can do this by sharing data as attachment of {@link InterceptorGroupInvocation}.
 *  When an InterceptorGroupInvocation is activated, interceptors in the group can share an attachment object.
 *  InterceptorGroupInvocation is activated when before() of any of interceptors in the group is invoked, but after() of it is not invoked.
 *  
 *  @see Sample_04_Interceptors_In_A_Group_Share_Value
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent(SampleTestConstants.AGENT_PATH)
@Dependency({"com.navercorp.pinpoint:plugin-sample-target:1.5.0-SNAPSHOT"})
public class Sample_04_Interceptors_In_A_Group_Share_Value_IT {

    @Test
    public void test() throws Exception {
        String name = "FOOBAR";
        int length = name.length();

        TargetClass04 target = new TargetClass04();
        target.outerMethod(name);
        
        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();
        
        Method targetMethod = TargetClass04.class.getMethod("outerMethod", String.class);
        verifier.verifyTrace(event("PluginExample", targetMethod, args(name)[0], annotation("MyValue", length)));
        
        // no more traces
        verifier.verifyTraceCount(0);
    }
}
