package com.navercorp.plugin.sample.target;

public class TargetClass14_Server {
    private final String address;
    
    public TargetClass14_Server(String address) {
        this.address = address;
    }

    public String process(TargetClass14_Request request) {
        return "Hello " + request.getArgument();
    }

    public String getAddress() {
        return address;
    }
}