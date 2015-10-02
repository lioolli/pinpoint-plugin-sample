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

/**
 * @author Jongho Moon
 *
 */
public class TargetClass13_Client {
    private final String serverAddress;
    private final int serverPort;
    
    public TargetClass13_Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }
    
    public String sendRequest(TargetClass13_Request request) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            //ignore
        }
        
        return "SUCCESS";
    }
}