package com.navercorp.target;

public class TargetClass3 {
    public void targetMethodA() {
        targetMethodB(10);
    }

    public void targetMethodB(int times) {
        if (times <= 0) {
            return;
        }

        targetMethodB(times - 1);
    }
}