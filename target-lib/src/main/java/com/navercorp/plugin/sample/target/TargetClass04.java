package com.navercorp.target;

public class TargetClass4 {
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