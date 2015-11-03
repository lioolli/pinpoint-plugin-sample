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
import com.navercorp.pinpoint.bootstrap.interceptor.AroundInterceptor2;
import com.navercorp.pinpoint.bootstrap.interceptor.annotation.IgnoreMethod;
import com.navercorp.pinpoint.bootstrap.interceptor.group.InterceptorGroup;

/**
 * This interceptor get AsyncTraceId from interceptor group invocation attachment and set it to the initializing object through AsyncTraceIdAccessor
 */
public class WorkerConstructorInterceptor implements AroundInterceptor2 {
    private final InterceptorGroup group;
    
    public WorkerConstructorInterceptor(InterceptorGroup group) {
        this.group = group;
    }

    @IgnoreMethod
    @Override
    public void before(Object target, Object arg0, Object arg1) {

    }

    @Override
    public void after(Object target, Object arg0, Object arg1, Object result, Throwable throwable) {
        AsyncTraceId asyncTraceId = (AsyncTraceId)group.getCurrentInvocation().getAttachment();
        ((AsyncTraceIdAccessor)target)._$PINPOINT$_setAsyncTraceId(asyncTraceId);
    }
}
