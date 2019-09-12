package com.youyuan.nio;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author zhangyu
 * @version 1.0
 * @description  根据join方法控制线程的执行顺序
 * @date 2019/6/21 15:37
 */
public class ThreadDemo {

    static Thread thread1=new Thread(()->{
        System.out.println("thread1开始执行");
    });

    static Thread thread2=new Thread(()->{
        System.out.println("thread2开始执行");
    });

    static Thread thread3=new Thread(()->{
        System.out.println("thread3开始执行");
    });

    //创建线程池
    static ExecutorService executorService= Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws InterruptedException {
        executorService.submit(thread1);
        executorService.submit(thread2);
        executorService.submit(thread3);

        executorService.shutdown();
    }

}
