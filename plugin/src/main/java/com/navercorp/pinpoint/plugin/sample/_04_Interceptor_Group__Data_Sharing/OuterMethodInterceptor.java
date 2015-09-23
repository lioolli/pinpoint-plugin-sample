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
package com.navercorp.pinpoint.plugin.sample.interceptor;

import com.navercorp.pinpoint.bootstrap.context.SpanEventRecorder;
import com.navercorp.pinpoint.bootstrap.context.Trace;
import com.navercorp.pinpoint.bootstrap.context.TraceContext;
import com.navercorp.pinpoint.bootstrap.instrument.AttachmentFactory;
import com.navercorp.pinpoint.bootstrap.interceptor.MethodDescriptor;
import com.navercorp.pinpoint.bootstrap.interceptor.SimpleAroundInterceptor;
import com.navercorp.pinpoint.bootstrap.interceptor.group.InterceptorGroup;
import com.navercorp.pinpoint.bootstrap.interceptor.group.InterceptorGroupInvocation;
import com.navercorp.pinpoint.bootstrap.logging.PLogger;
import com.navercorp.pinpoint.bootstrap.logging.PLoggerFactory;
import com.navercorp.pinpoint.plugin.sample.MyAttachment;
import com.navercorp.pinpoint.plugin.sample.MyPlugin;

/**
 * This interceptor attach an object to current {@link InterceptorGroupInvocation}.
 * The attachment helps the interceptor collaborates with {@link Sample4_InnerMethodInterceptor}.
 * 
 * @see MyPlugin#example4_Interceptors_In_A_Group_Share_Value(com.navercorp.pinpoint.bootstrap.plugin.ProfilerPluginContext)
 * @author Jongho Moon
 */
public class Sample4_OuterMethodInterceptor implements SimpleAroundInterceptor {
    private static final AttachmentFactory ATTACHMENT_FACTORY = new AttachmentFactory() {
        
        @Override
        public Object createAttachment() {
            return new MyAttachment();
        }
    };
    
    private final PLogger logger = PLoggerFactory.getLogger(getClass());
    private final boolean isDebug = logger.isDebugEnabled();

    private final MethodDescriptor descriptor;
    private final TraceContext traceContext;
    private final InterceptorGroup group;

    public Sample4_OuterMethodInterceptor(TraceContext traceContext, MethodDescriptor descriptor, InterceptorGroup group) {
        this.descriptor = descriptor;
        this.traceContext = traceContext;
        this.group = group;
    }
    
    @Override
    public void before(Object target, Object[] args) {
        if (isDebug) {
            logger.beforeInterceptor(target, args);
        }

        Trace trace = traceContext.currentTraceObject();
        if (trace == null) {
            return;
        }
        
        String arg0 = (String)args[0];
        boolean shouldTrace = arg0.startsWith("FOO");
        
        if (!shouldTrace) {
            return;
        }

        SpanEventRecorder recorder = trace.traceBlockBegin();
        recorder.recordServiceType(MyPlugin.MY_SERVICE_TYPE);

        // create attachment
        MyAttachment attachment = (MyAttachment)group.getCurrentInvocation().getOrCreateAttachment(ATTACHMENT_FACTORY);
        attachment.setTrace(shouldTrace);
    }

    @Override
    public void after(Object target, Object[] args, Object result, Throwable throwable) {
        if (isDebug) {
            logger.afterInterceptor(target, args);
        }

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
            
            recorder.recordApi(descriptor, args);
            recorder.recordException(throwable);
            
            // record the value set by Sample4_InnerMethodInterceptor
            recorder.recordAttribute(MyPlugin.ANNOTATION_KEY_MY_VALUE, attachment.getValue());
        } finally {
            trace.traceBlockEnd();
        }
    }
}
