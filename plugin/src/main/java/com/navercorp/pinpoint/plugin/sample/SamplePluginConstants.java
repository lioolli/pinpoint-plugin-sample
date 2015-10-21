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

import com.navercorp.pinpoint.common.trace.AnnotationKey;
import com.navercorp.pinpoint.common.trace.AnnotationKeyFactory;
import com.navercorp.pinpoint.common.trace.ServiceType;
import com.navercorp.pinpoint.common.trace.ServiceTypeFactory;

/**
 * @author Jongho Moon
 *
 */
public interface SamplePluginConstants {

    public static final ServiceType MY_SERVICE_TYPE = ServiceTypeFactory.of(7500, "PluginExample");
    public static final AnnotationKey ANNOTATION_KEY_MY_VALUE = AnnotationKeyFactory.of(998, "MyValue");
    
    public static final ServiceType MY_RPC_SERVER_SERVICE_TYPE = ServiceTypeFactory.of(1900, "SAMPLE_SERVER");
    
    public static final ServiceType MY_RPC_CLIENT_SERVICE_TYPE = ServiceTypeFactory.of(9901, "SAMPLE_CLIENT");
    public static final AnnotationKey MY_RPC_ARGUMENT_ANNOTATION_KEY = AnnotationKeyFactory.of(995, "MY_ARGUMENT");
    public static final AnnotationKey MY_RPC_PROCEDURE_ANNOTATION_KEY = AnnotationKeyFactory.of(996, "MY_PROCEDURE");
    public static final AnnotationKey MY_RPC_RESULT_ANNOTATION_KEY = AnnotationKeyFactory.of(997, "MY_RESULT");

    public static final String META_DO_NOT_TRACE = "_SAMPLE_DO_NOT_TRACE";
    public static final String META_TRANSACTION_ID = "_SAMPLE_TRASACTION_ID";
    public static final String META_SPAN_ID = "_SAMPLE_SPAN_ID";
    public static final String META_PARENT_SPAN_ID = "_SAMPLE_PARENT_SPAN_ID";
    public static final String META_PARENT_APPLICATION_NAME = "_SAMPLE_PARENT_APPLICATION_NAME";
    public static final String META_PARENT_APPLICATION_TYPE = "_SAMPLE_PARENT_APPLICATION_TYPE";
    public static final String META_FLAGS = "_SAMPLE_FLAGS";
    
}
