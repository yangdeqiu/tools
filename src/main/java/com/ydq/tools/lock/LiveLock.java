package com.ydq.tools.lock;

import java.util.concurrent.TimeUnit;

/**
 * 活锁演示
 */
public class LiveLock {

    public static void main(String[] args) {
        People gentleman1 = new People("绅士1");
        People gentleman2 = new People("绅士2");

        Action getUpAction = new Action(gentleman1);

        new Thread(() -> {
            try {
                System.out.println("线程1-绅士2...");
                gentleman1.doAction(getUpAction, gentleman2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                System.out.println("线程2-绅士1...");
                gentleman2.doAction(getUpAction, gentleman1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}

class Action {
    private People executor;

    public Action(People executor) {
        this.executor = executor;
    }

    public synchronized void execute() {
        System.out.println(executor.getName() + " 执行起身动作");
    }

    public People getexecutor() {
        return executor;
    }

    public void setexecutor(People executor) {
        this.executor = executor;
    }
}

class People {
    private String name; // 姓名
    private boolean isBendOver; // 是否弯腰

    public People(String name) {
        this.name = name;
        this.isBendOver = true;
    }

    public void doAction(Action action, People other) throws InterruptedException {
        while (isBendOver) {
            // 如果对方正在起身，等对方先起身
            if (action.getexecutor() != this) {
                TimeUnit.MILLISECONDS.sleep(1);
                continue;
            }
            // 如果对方仍在弯腰，请对方起身
            if (other.isBendOver) {
                // 使用随机元素打破活锁
                // Random rand = new Random();
                // if (other.isBendOver && rand.nextInt(10) < 9) {
                System.out.println(name + "对" + other.getName() + "说，您先起身。");
                action.setexecutor(other);
                continue;
            }
            action.execute();
            isBendOver = false;
            System.out.println(name + ":我起身了");
            action.setexecutor(other);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}