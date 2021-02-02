package com.ydq.tools.jvm.oom;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * jvm调优 - oom - 问题1
 */
public class ExecutorOOM_problem_1 {

    private static class CardInfo {
        BigDecimal price = new BigDecimal(0.0);
        String name = "张三";
        long age = 5L;
        Date brithDay = new Date();
        public void m() {}
    }

    // 工作队列过大导致OOM
    private static ScheduledThreadPoolExecutor executor1 =
            new ScheduledThreadPoolExecutor(50, new ThreadPoolExecutor.DiscardOldestPolicy());

    // 新建线程池需要使用ThreadPoolExecutor方式创建
    private static ExecutorService executor = new ThreadPoolExecutor(5, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(50), new ThreadPoolExecutor.AbortPolicy());


    public static void main(String agrs[]) throws InterruptedException {
        executor1.setMaximumPoolSize(50);
        for (;;) {
            modelFit();
            Thread.sleep(10);
        }
    }

    private static void modelFit() {
//        System.out.println(String.format("== start - %s ==", System.currentTimeMillis()));
        List<CardInfo> taskList = getAllCardInfo();
        // 产生OOM
        taskList.forEach(info -> executor1.scheduleWithFixedDelay(info::m, 2, 3, TimeUnit.SECONDS));
        // 不产生OOM
//        taskList.forEach( info -> executor.execute(new Thread(info::m)));
    }

    private static List<CardInfo> getAllCardInfo() {
        List<CardInfo> taskList = new ArrayList<>();
        for (int i = 0; i < 10000; i ++) {
            CardInfo ci = new CardInfo();
            taskList.add(ci);
        }
        return taskList;
    }

}
