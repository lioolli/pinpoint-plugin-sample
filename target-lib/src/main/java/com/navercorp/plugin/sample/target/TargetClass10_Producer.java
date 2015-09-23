package com.navercorp.plugin.sample.target;


public class TargetClass10_Producer {
    private final String name;

    public TargetClass10_Producer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public TargetClass10_Message produce() {
        return new TargetClass10_Message();
    }
}