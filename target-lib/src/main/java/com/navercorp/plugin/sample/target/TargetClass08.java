package com.navercorp.plugin.sample.target;

public class TargetClass08 {
    public String targetMethod() {
        return targetMethod("PINPOINT");
    }
    
    public String targetMethod(String name) {
        return "Hello " + name;
    }
}