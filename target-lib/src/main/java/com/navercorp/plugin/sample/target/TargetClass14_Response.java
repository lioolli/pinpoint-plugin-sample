package com.navercorp.plugin.sample.target;

public class TargetClass14_Response {
    private final String resultCode;
    private final String response;
    
    public TargetClass14_Response(String resultCode, String response) {
        this.resultCode = resultCode;
        this.response = response;
    }

    public String getResultCode() {
        return resultCode;
    }

    public String getResponse() {
        return response;
    }
}