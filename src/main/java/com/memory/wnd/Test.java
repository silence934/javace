package com.memory.wnd;

/**
 * @author: silence
 * @Date: 2021/9/3 00:13
 * @Description:
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("xxx");
        });


        thread.start();
        Thread.sleep(500);
        thread.start();

    }
}
