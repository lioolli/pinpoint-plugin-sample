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
package com.navercorp.pinpoint.plugin.sample._04_Interceptor_Group__Data_Sharing;

import com.navercorp.pinpoint.bootstrap.context.MethodDescriptor;
import com.navercorp.pinpoint.bootstrap.context.SpanEventRecorder;
import com.navercorp.pinpoint.bootstrap.context.Trace;
import com.navercorp.pinpoint.bootstrap.context.TraceContext;
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor1;
import com.navercorp.pinpoint.bootstrap.interceptor.group.AttachmentFactory;
import com.navercorp.pinpoint.bootstrap.interceptor.group.InterceptorGroup;
import com.navercorp.pinpoint.bootstrap.interceptor.group.InterceptorGroupInvocation;
import com.navercorp.pinpoint.plugin.sample.MyPluginConstants;

/**
 * This interceptor attach an object to current {@link InterceptorGroupInvocation}.
 * The attachment helps the interceptor collaborates with {@link InnerMethodInterceptor}.
 * 
 * @see Sample_04_Interceptors_In_A_Group_Share_Value
 * @author Jongho Moon
 */
public class OuterMethodInterceptor implements AroundInterceptor1 {
    private static final AttachmentFactory ATTACHMENT_FACTORY = new AttachmentFactory() {
        
        @Override
        public Object createAttachment() {
            return new MyAttachment();
        }
    };
    
    private final MethodDescriptor descriptor;
    private final TraceContext traceContext;
    private final InterceptorGroup group;

    // An interceptor receives an InterceptorGroup through its constructor
    public OuterMethodInterceptor(TraceContext traceContext, MethodDescriptor descriptor, InterceptorGroup group) {
        this.descriptor = descriptor;
        this.traceContext = traceContext;
        this.group = group;
    }
    
    @Override
    public void before(Object target, Object arg0) {
        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }
        
        boolean shouldTrace = ((String)arg0).startsWith("FOO");
        
        if (!shouldTrace) {
            return;
        }

        SpanEventRecorder recorder = trace.traceBlockBegin();
        recorder.recordServiceType(MyPluginConstants.MY_SERVICE_TYPE);

        // create or get attachment
        MyAttachment attachment = (MyAttachment)group.getCurrentInvocation().getOrCreateAttachment(ATTACHMENT_FACTORY);
        attachment.setTrace(shouldTrace);
    }

    @Override
    public void after(Object target, Object result, Throwable throwable, Object arg0) {
        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }
        
        try {
            MyAttachment attachment = (MyAttachment)group.getCurrentInvocation().getAttachment();
            
            if (!attachment.isTrace()) {
                return; 
            }
            
            SpanEventRecorder recorder = trace.currentSpanEventRecorder();
            
            recorder.recordApi(descriptor, new Object[] { arg0 });
            recorder.recordException(throwable);
            
            // record the value set by InnerMethodInterceptor
            recorder.recordAttribute(MyPluginConstants.ANNOTATION_KEY_MY_VALUE, attachment.getValue());
        } finally {
            trace.traceBlockEnd();
        }
    }
}
