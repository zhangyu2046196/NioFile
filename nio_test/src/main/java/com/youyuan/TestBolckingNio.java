package com.youyuan;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author zhangyu
 * @version 1.0
 * @description nio测试阻塞式io
 *
 * java.nio.channels.Channel
 *  Channel ：接口
 *      |-- SelectablleChannel       抽象类
 *          |-- SocketChannel        通过tcp获取网络数据
 *          |-- ServerSocketChannel  通过tcp获取网络数据
 *          |-- DatagramChannel      通过udp获取网络数据
 *
 * @date 2019/3/14 22:29
 */
public class TestBolckingNio {

    /**
     * 测试网络通信客户端
     */
    @Test
    public void client() throws IOException {
        //获取 网络的SocketChannel
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9899));
        //获取读取文件的FileChannel
        FileChannel fChannel = FileChannel.open(Paths.get("1.jpg"),StandardOpenOption.READ);
        //创建缓冲区
        ByteBuffer buf=ByteBuffer.allocate(1024);
        //通过通道FileChannel循环读取数据放入SocketChannel
        while(fChannel.read(buf)!=-1){
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }

        fChannel.close();
        sChannel.close();

    }

    /**
     * 测试网络通信服务端
     */
    @Test
    public void server() throws IOException {
        //获取服务端ServerSocketChannel
        ServerSocketChannel ssChannel = ServerSocketChannel.open();
        //获取FileChannel
        FileChannel fChannel = FileChannel.open(Paths.get("6.jpg"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);
        //绑定连接
        ssChannel.bind(new InetSocketAddress(9899));
        //获取客户端连接的通道
        SocketChannel accept = ssChannel.accept();
        //创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //循环读取数据写入到FileChannel
        while (accept.read(buffer)!=-1){
            buffer.flip();
            fChannel.write(buffer);
            buffer.clear();
        }
        accept.close();
        fChannel.close();

        ssChannel.close();
    }

}
