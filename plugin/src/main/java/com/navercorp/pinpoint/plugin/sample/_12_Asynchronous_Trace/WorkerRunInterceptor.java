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
package com.navercorp.pinpoint.plugin.sample._12_Asynchronous_Trace;

import com.navercorp.pinpoint.bootstrap.async.AsyncTraceIdAccessor;
import com.navercorp.pinpoint.bootstrap.context.AsyncTraceId;
import com.navercorp.pinpoint.bootstrap.context.MethodDescriptor;
import com.navercorp.pinpoint.bootstrap.context.SpanEventRecorder;
import com.navercorp.pinpoint.bootstrap.context.TraceContext;
import com.navercorp.pinpoint.bootstrap.instrument.InstrumentClass;
import com.navercorp.pinpoint.bootstrap.interceptor.SpanAsyncEventSimpleAroundInterceptor;
import com.navercorp.pinpoint.plugin.sample.SamplePluginConstants;

/**
 * This interceptor intercepts run() method, which handles the async task.
 * 
 * Pinpoint provides {@link SpanAsyncEventSimpleAroundInterceptor} which handles all the chores related to the async trace.
 * You can extends it and forget about all the async things except for one.
 * {@link SpanAsyncEventSimpleAroundInterceptor} retrieves {@link AsyncTraceId} from the target objcet through {@link AsyncTraceIdAccessor}.
 * Therefore you have to add it to the target class by {@link InstrumentClass#addField(String)}.
 * Of course, don't forget setting AsyncTraceIdAccessor before this interceptor is executed.
 * In this sample, {@link WorkerConstructorInterceptor} takes care of it. 
 * 
 * @author Jongho Moon
 */
public class WorkerRunInterceptor extends SpanAsyncEventSimpleAroundInterceptor {

    public WorkerRunInterceptor(TraceContext traceContext, MethodDescriptor methodDescriptor) {
        super(traceContext, methodDescriptor);
    }

    @Override
    protected void doInBeforeTrace(SpanEventRecorder recorder, AsyncTraceId asyncTraceId, Object target, Object[] args) {
        // do nothing
    }

    @Override
    protected void doInAfterTrace(SpanEventRecorder recorder, Object target, Object[] args, Object result, Throwable throwable) {
        recorder.recordServiceType(SamplePluginConstants.MY_SERVICE_TYPE);
        recorder.recordApi(methodDescriptor);
        recorder.recordException(throwable);
    }
}
