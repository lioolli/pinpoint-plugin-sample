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
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifierHolder;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import com.navercorp.pinpoint.test.plugin.TraceObjectManagable;
import com.navercorp.plugin.sample.target.TargetClass14_Request;
import com.navercorp.plugin.sample.target.TargetClass14_Server;

/**
 *  
 * 
 * @see Sample_14_RPC_Server_IT
 * @author Jongho Moon
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent(SampleTestConstants.AGENT_PATH)
@Dependency({"com.navercorp.pinpoint:plugin-sample-target:1.5.0-SNAPSHOT"})
@TraceObjectManagable
public class Sample_14_RPC_Server_IT {

    @Test
    public void test() throws Exception {
        Map<String, String> metadatas = new HashMap<String, String>();
        metadatas.put("_SAMPLE_TRASACTION_ID", "1234567890");
        metadatas.put("_SAMPLE_SPAN_ID", "9876543210");
        metadatas.put("_SAMPLE_PARENT_SPAN_ID", "1357913579");
        metadatas.put("_SAMPLE_PARENT_SPAN_ID", "1357913579");
        metadatas.put("_SAMPLE_PARENT_APPLICATION_NAME", "sample.client");
        metadatas.put("_SAMPLE_PARENT_APPLICATION_TYPE", "1000");
        metadatas.put("_SAMPLE_FLAGS", "0");
        
        TargetClass14_Server server = new TargetClass14_Server("1.2.3.4");
        TargetClass14_Request request = new TargetClass14_Request("5.6.7.8", "sample.pinpoint.navercorp.com", "hello", "pinpoint", metadatas);
        
        server.process(request);
        
        Method sendRequest = TargetClass14_Server.class.getMethod("process", TargetClass14_Request.class);

        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache();

        verifier.verifyTrace(event("SAMPLE_SERVER", sendRequest, null, "1.2.3.4:5678", "sample", annotation("MY_PROCEDURE", "hello"), annotation("MY_ARGUMENT", "pinpoint"), annotation("MY_RESULT", "SUCCESS")));

        verifier.verifyTraceCount(0);
    }
}
