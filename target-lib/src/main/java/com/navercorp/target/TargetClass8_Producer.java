package com.navercorp.target;


public class TargetClass8_Producer {
    private final String name;

    public TargetClass8_Producer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TargetClass8_Message produce() {
        return new TargetClass8_Message();
    }
}