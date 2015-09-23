package com.navercorp.plugin.sample.target;

public class TargetClass04 {
    private int sum = 0;

    public void outerMethod(String name) {
        int len = innerMethod(name);
        sum += len;
    }

    private int innerMethod(String name) {
        return name.length();
    }

    public int getSum() {
        return sum;
    }
}