package com.navercorp.plugin.sample.target;

public class TargetClass09 {
    private final String hiddenField;

    public TargetClass09(String hiddenField) {
        this.hiddenField = hiddenField;
    }
    
    public void targetMethod() {
        
    }

    @Override
    public String toString() {
        return "TargetClass09 [hiddenField=" + hiddenField + "]";
    }
}