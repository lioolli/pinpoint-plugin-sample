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

import com.navercorp.pinpoint.common.trace.TraceMetadataProvider;
import com.navercorp.pinpoint.common.trace.TraceMetadataSetupContext;

/**
 * @author Jongho Moon
 *
 */
public class SampleTraceMetadataProvider implements TraceMetadataProvider {

    /* (non-Javadoc)
     * @see com.navercorp.pinpoint.common.trace.TraceMetadataProvider#setup(com.navercorp.pinpoint.common.trace.TraceMetadataSetupContext)
     */
    @Override
    public void setup(TraceMetadataSetupContext context) {
        context.addServiceType(SamplePluginConstants.MY_SERVICE_TYPE);
        context.addAnnotationKey(SamplePluginConstants.ANNOTATION_KEY_MY_VALUE);
        
        context.addServiceType(SamplePluginConstants.MY_RPC_SERVER_SERVICE_TYPE);
        context.addServiceType(SamplePluginConstants.MY_RPC_CLIENT_SERVICE_TYPE);
        context.addAnnotationKey(SamplePluginConstants.MY_RPC_PROCEDURE_ANNOTATION_KEY);
        context.addAnnotationKey(SamplePluginConstants.MY_RPC_ARGUMENT_ANNOTATION_KEY);
        context.addAnnotationKey(SamplePluginConstants.MY_RPC_RESULT_ANNOTATION_KEY);
    }

}
