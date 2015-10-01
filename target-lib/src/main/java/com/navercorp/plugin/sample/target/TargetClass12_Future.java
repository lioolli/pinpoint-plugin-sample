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
package com.navercorp.plugin.sample.target;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TargetClass12_Future {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private String value;
    
    public String get() throws InterruptedException {
        lock.lock();
        
        try {
            if (value == null) {
                condition.await();
            }
            
            return value;
        } finally {
            lock.unlock();
        }
    }
    
    void set(String value) {
        lock.lock();
        
        try {
            this.value = value;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}