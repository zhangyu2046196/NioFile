package com.youyuan.nio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author zhangyu
 * @version 1.0
 * @description 阻塞IO测试服务端代码
 * @date 2019/6/20 20:18
 */
public class TradtionDemo {

    public static void main(String[] args) throws IOException {
        //创建网络编程服务端  参数端口号
        ServerSocket serverSocket=new ServerSocket(8989);
        System.out.println("服务端已启动......");
        //获取客户端连接
        Socket accept = serverSocket.accept();
        System.out.println("获取客户端请求.....");
        //获取客户端请求参数
        InputStream inputStream = accept.getInputStream();
        //创建存储客户端请求数据
        byte[] bytes=new byte[1024];
        while (true){
            //将数据读取到byte数组
            int read = inputStream.read(bytes);
            String res=new String(bytes,0,read,"GBK");
            System.out.println(res);
            if (read==-1){
                break;
            }
        }
    }

}
