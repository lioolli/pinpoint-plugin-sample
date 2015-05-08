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

import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifier.BlockType;
import com.navercorp.pinpoint.bootstrap.plugin.test.PluginTestVerifierHolder;
import com.navercorp.pinpoint.plugin.sample.MyPlugin;
import com.navercorp.pinpoint.test.plugin.Dependency;
import com.navercorp.pinpoint.test.plugin.PinpointAgent;
import com.navercorp.pinpoint.test.plugin.PinpointPluginTestSuite;
import com.navercorp.target.TargetClass8_Consumer;
import com.navercorp.target.TargetClass8_Message;
import com.navercorp.target.TargetClass8_Producer;

/**
 * We want to trace {@link TargetClass8_Consumer#consume(TargetClass8_Message)} with producer name.
 * But we can not retrieve the producer name in the method. 
 * So we intercept {@link TargetClass8_Producer#produce()} to inject producer name into the returning {@link TargetClass8_Message}. 
 * 
 * @see MyPlugin#sample8_Inject_Metadata(com.navercorp.pinpoint.bootstrap.plugin.ProfilerPluginContext)
 * @author Jongho Moon
 */
@RunWith(PinpointPluginTestSuite.class)
@PinpointAgent("target/my-pinpoint-agent")
@Dependency({"com.navercorp.pinpoint:plugin-example-target:[1.0.0,)"})
public class Sample8_Inject_Metadata_IT {

    @Test
    public void test() throws Exception {
        String name = "Pinpoint";
        
        TargetClass8_Producer producer = new TargetClass8_Producer(name);
        TargetClass8_Consumer consumer = new TargetClass8_Consumer();
        
        consumer.consume(producer.produce());


        PluginTestVerifier verifier = PluginTestVerifierHolder.getInstance();
        verifier.printCache(System.out);
        verifier.printBlocks(System.out);
        
        Method targetMethod = TargetClass8_Consumer.class.getMethod("consume", TargetClass8_Message.class);
        
        verifier.verifyTraceBlock(BlockType.EVENT, "PluginExample", targetMethod, null, null, null, null, annotation("MyValue", name));
        
        // no more traces
        verifier.verifyTraceBlockCount(0);
    }
}
