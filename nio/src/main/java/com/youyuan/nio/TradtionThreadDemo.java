package com.youyuan.nio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author zhangyu
 * @version 1.0
 * @description 通过多线程解决IO阻塞问题
 * @date 2019/6/20 22:36
 */
public class TradtionThreadDemo {

    public static void main(String[] args) throws IOException {
        //创建线程池
        //ExecutorService executorService=new ThreadPoolExecutor(10,20,10, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(5));
        ExecutorService executorService=Executors.newCachedThreadPool();
        //创建网络编程
        ServerSocket serverSocket=new ServerSocket(8989);
        System.out.println("服务端已启动 ......");

        while(true){
            //多线程执行 lambda方式
            executorService.submit(()->{
                //创建接收数据byte数组;
                byte[] bytes=new byte[1024];
                try {
                    //获取客户端
                    Socket accept = serverSocket.accept();
                    System.out.println("接收客户端请求 ...... ");
                    while(true){
                        //获取客户端输入流
                        InputStream inputStream = accept.getInputStream();
                        //读取客户端数据
                        int read = inputStream.read(bytes);
                        if (read!=-1){
                            String res=new String(bytes,0,read,"GBK");
                            System.out.println(res);
                        }else {
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }

}
