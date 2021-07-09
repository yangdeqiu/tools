package com.ydq.tools.lock;

import java.util.concurrent.TimeUnit;

/**
 * 死锁演示
 */
public class MustDeadLock implements Runnable {
    private int flag;

    public MustDeadLock(int flag) {
        this.flag = flag;
    }

    private static final Object resourceA = new Object();
    private static final Object resourceB = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(new MustDeadLock(0));
        Thread thread2 = new Thread(new MustDeadLock(1));
        thread1.start();
        thread2.start();
    }

    @Override
    public void run() {
        if (flag == 0) {
            synchronized (resourceA) {
                System.out.println(Thread.currentThread().getName() + "获取A锁");
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (resourceB) {
                    System.out.println(Thread.currentThread().getName() + "获取B锁");
                }
            }
        } else {
            synchronized (resourceB) {
                System.out.println(Thread.currentThread().getName() + "获取B锁");
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (resourceA) {
                    System.out.println(Thread.currentThread().getName() + "获取A锁");
                }
            }
        }
    }
}