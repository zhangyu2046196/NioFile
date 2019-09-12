package com.youyuan.nio;

/**
 * @author zhangyu
 * @version 1.0
 * @description 测试编译类来查看字节码文件
 * @date 2019/6/25 16:46
 */
public class SynchronizedTest {

    public static void main(String[] args) {
        synchronized (SynchronizedTest.class){

        }
        test();
    }

    static synchronized void test(){
        System.out.println("...");
    }

}
