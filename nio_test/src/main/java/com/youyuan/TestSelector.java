package com.youyuan;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author zhangyu
 * @version 1.0
 * @description 选择器测试类信息
 * @date 2019/3/15 11:50
 */
public class TestSelector {

    /**
     * 非阻塞io客户端
     */
    @Test
    public void client() throws IOException {
        //创建channel
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9899));
        //设置为非阻塞模式
        sChannel.configureBlocking(false);
        //创建缓冲区
        ByteBuffer buffer=ByteBuffer.allocate(1024);
        Scanner scanner=new Scanner(System.in);
        while (scanner.hasNext()){
            String str=scanner.next();
            buffer.put((new Date().toString()+str).getBytes());
            //设置缓冲区读模式
            buffer.flip();
            //缓冲区数据写入channel
            sChannel.write(buffer);
            buffer.clear();
        }
        //关闭连接
        sChannel.close();
    }

    /**
     * 非阻塞io服务端
     */
    @Test
    public void server() throws IOException {
        //获取channel
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //设置通道为非阻塞模式
        ssChannel.configureBlocking(false);
        //设置通道绑定的端口号
        ssChannel.bind(new InetSocketAddress(9899));
        //创建选择器
        Selector selector = Selector.open();
        //将通道注册到选择器,监听接收状态
        ssChannel.register(selector, SelectionKey.OP_ACCEPT);
        //轮询的获取选择器上已经就绪的事件
        while (selector.select()>0){
            //获取选择器上监听的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey=iterator.next();
                //如果选择器的监听事件是接收模式
                if (selectionKey.isAcceptable()){
                    //获取客户端连接
                    SocketChannel sChannel = ssChannel.accept();
                    //设置客户端连接为非阻塞模式
                    sChannel.configureBlocking(false);
                    //客户端端连接注册到选择器上,设置读模式事件
                    sChannel.register(selector,SelectionKey.OP_READ);
                }else if (selectionKey.isReadable()){//如果选择器的事件类型读模式
                    //获取当前选择器上读就绪的通道
                    SocketChannel sChannel = (SocketChannel) selectionKey.channel();
                    //建立缓冲区
                    ByteBuffer buf = ByteBuffer.allocate(1024);
                    while (sChannel.read(buf)!=-1){
                        buf.flip();
                        System.out.println("结果:"+new String(buf.array()));
                        buf.clear();
                    }
                }
                //取消选择键
                iterator.remove();
            }
        }
    }

}
