package com.youyuan;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @author zhangyu
 * @version 1.0
 * @description 通道测试类信息
 * 一、Channel是java.nio.channel包下面的类，用于对源和目标创建连接(类似铁路),负责对buffer数据传输,本身不存储数据，只能与buffer交互
 * 二、Channel分类
 *      channel是java.nio.channel包下面的接口，实现类有
 *         |--  FileChannel     对文件操作的通道
 *         |--  DatagramChannel 通过udp对网络数据操作的通道
 *         |--  SocketChannel   通过tcp对网络数据操作的通道
 *         |--  ServerSocketChannel  监听socket的连接，如果有新的socket连接进来，分配一个新的SocketChannel通道
 * 三、获取通道方式
 *  1、本地IO
 *      FileInputStream
 *      FileOutputStream
 *      RandomaccessFile
 *  2、网络IO
 *      Socket
 *      ServerSocket
 *      DatagramSocket
 *  3、jdk1.7中的针对各个通道提供的静态方法open()
 *  4、jdk1.7中Files工具类中的newByteChannel()
 *
 * 四、通道传输数据
 *  1、transferFrom
 *  2、transferTo
 * 五、分散读取和聚集写入
 *  1、Scatter Reads  分散读取
 *  2、Gather Writers 聚集写入
 *
 * 六、编码 和 解码
 *  编码：字符串转成字节
 *  解码：字节转成字符串
 *  Charset编码解码器
 *
 * @date 2019/3/14 14:48
 */
public class TestChannel {

    /**
     * 编码解码测试
     */
    @Test
    public void test6() throws CharacterCodingException {
        //设置编码解码器的字符集
        Charset gbk = Charset.forName("GBK");
        //获取编码器
        CharsetEncoder charsetEncoder = gbk.newEncoder();
        //获取解码器
        CharsetDecoder charsetDecoder = gbk.newDecoder();
        //创建缓冲区
        CharBuffer allocate = CharBuffer.allocate(1024);
        allocate.put("北京友缘在线网络科技股份有限公司");
        allocate.flip();
        //编码
        ByteBuffer encode = charsetEncoder.encode(allocate);
        for (int i=0;i<encode.limit();i++){
            System.out.println(encode.get());
        }

        //解码
        encode.flip();
        CharBuffer decode = charsetDecoder.decode(encode);
        System.out.println(decode);
    }


    /**
     * 分散读取和聚集写入
     */
    @Test
    public void test5() throws IOException {
        RandomAccessFile raf=new RandomAccessFile("1.txt","rw");
        //创建通道
        FileChannel channel = raf.getChannel();
        //创建缓冲区数据用于接收channel中读取的数据
        ByteBuffer buffer1=ByteBuffer.allocate(100);
        ByteBuffer buffer2=ByteBuffer.allocate(1024);
        ByteBuffer[] bts=new ByteBuffer[]{buffer1,buffer2};
        //从channel读取数据到缓冲区
        channel.read(bts);
        //遍历缓冲区，将缓冲区设置为读模式
        for (ByteBuffer bt:bts){
            bt.flip();
        }
        //遍历缓冲区信息内容
        System.out.println(new String(buffer1.array()));
        System.out.println("-----------------");
        System.out.println(new String(buffer2.array()));

        //聚集写入
        RandomAccessFile ra=new RandomAccessFile("2.txt","rw");
        //创建通道
        FileChannel channe = ra.getChannel();
        //将缓冲区内容写入通道
        channe.write(bts);
    }

    /**
     * 通过通道的transferFrom或transferTo传递数据
     */
    @Test
    public void test3() throws IOException {
        //获取通道
        FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("5.jpg"), StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE_NEW);

        //传递数据有两种方式
        inChannel.transferTo(0,inChannel.size(),outChannel);
    }

    /**
     * 通道内存映射文件完成文件复制
     */
    @Test
    public void test2() throws IOException {
        //获取通道
        FileChannel inChannel = FileChannel.open(Paths.get("1.jpg"), StandardOpenOption.READ);
        FileChannel outChannel = FileChannel.open(Paths.get("3.jpg"), StandardOpenOption.READ,StandardOpenOption.WRITE,StandardOpenOption.CREATE_NEW);
        //创建内存映射文件MappedByteBuffer
        MappedByteBuffer inBuffer= inChannel.map(FileChannel.MapMode.READ_ONLY,0,inChannel.size());
        MappedByteBuffer outBuffer = outChannel.map(FileChannel.MapMode.READ_WRITE,0,inChannel.size());
        //对缓冲数据读写操作
        byte[] dst=new byte[inBuffer.limit()];
        //数据读取到byte数组中
        inBuffer.get(dst);
        //byte数组中数据写入内存映射文件
        outBuffer.put(dst);

        outChannel.close();

        inChannel.close();
    }

    /**
     * 利用通道完成文件复制
     */
    @Test
    public void test1() throws IOException {
        //声明本地io流，通过本地io流对象的getChannel获取通道
        FileInputStream inStream=new FileInputStream("1.jpg");
        FileOutputStream outStream=new FileOutputStream("2.jpg");
        //获取通道
        FileChannel inChannel= inStream.getChannel();
        FileChannel outChannel=outStream.getChannel();
        //创建指定大小的缓冲区
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        while(inChannel.read(allocate)!=-1){
            //切换缓冲区为读模式
            allocate.flip();
            //将缓冲区信息写入到通道
            outChannel.write(allocate);
            //每次buffer读取的信息写入通道后清空
            allocate.clear();
        }
        outChannel.close();
        inChannel.close();
        outStream.close();
        inStream.close();
    }
}
