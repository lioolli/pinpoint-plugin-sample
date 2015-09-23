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

import com.navercorp.pinpoint.bootstrap.interceptor.AfterInterceptor0;
import com.navercorp.pinpoint.bootstrap.interceptor.group.InterceptorGroup;
import com.navercorp.pinpoint.bootstrap.interceptor.group.InterceptorGroupInvocation;

/**
 * This interceptor uses {@link InterceptorGroupInvocation} attachment to pass data to {@link OuterMethodInterceptor}
 */
public class InnerMethodInterceptor implements AfterInterceptor0 {
    private final InterceptorGroup group;

    // An interceptor receives an InterceptorGroup through its constructor
    public InnerMethodInterceptor(InterceptorGroup group) {
        this.group = group;
    }
    
    @Override
    public void after(Object target, Object result, Throwable throwable) {
        MyAttachment attachment = (MyAttachment)group.getCurrentInvocation().getAttachment();
        
        if (attachment.isTrace()) {
            attachment.setValue(result);
        }
    }
}
