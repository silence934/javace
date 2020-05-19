package com;

public class Aa {

    static class One{
        volatile String bb;
        volatile int cc;

    }

    public static void main(String[] args) {

        One one = new One();
        one.bb = "hewei";
        one.cc = 9999;

        System.out.println(Integer.toHexString(System.identityHashCode(one.cc)));
        System.out.println(Integer.toHexString(System.identityHashCode(one.bb)));


        int i = 0;
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (i++ % 10 == 0) {
                one.cc += 1;
            }
            System.out.println(one.cc + "|" + one.bb);
        }
    }
}
