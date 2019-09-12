package com.youyuan;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

/**
 * @author zhangyu
 * @version 1.0
 * @description 缓冲区测试
 * 一、缓冲区buffer用于存储数据，底层是数组
 * 二、根据数据类型可将缓冲区分为以下几类
 *  1、ByteBuffer
 *  2、ShortBuffer
 *  3、IntBuffer
 *  4、LongBuffer
 *  5、FloatBuffer
 *  6、StringBuffer
 *  7、DoubleBuffer
 * 三、通过调用allocate()方法获取缓冲区
 * 四、操作缓冲区的两个重要方法
 *  1、put() 数据写入缓冲区
 *  2、get() 从缓冲区读取数据
 * 五、缓冲区的四个属性
 *  1、capacity 容量  缓冲区最多可以存储数据的容量
 *  2、limit 边界  缓冲区中可操作数据的大小
 *  3、position 位置  当前操作缓冲区的索引位置 位置从0开始
 *  4、make 记录当前position的位置
 *    reset 恢复position的位置为make记录时的位置
 * 六、直接缓冲区和非直接缓冲区
 *  1、非直接缓冲区通过allocate()方法创建，缓冲区建立在JVM虚拟机内存中
 *  2、直接缓冲区通过allocateDirect()方法创建，缓冲区建立在物理内存中
 * @date 2019/3/13 22:28
 */
public class TestBuffer {

    @Test
    public void test2(){
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        System.out.println(buffer.isDirect());
    }

    @Test
    public void test1(){
        //创建一个指定大小的ByteBuffer缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        System.out.println("=============allocate==============");
        System.out.println("position:"+buffer.position());
        System.out.println("limit:"+buffer.limit());
        System.out.println("capacity:"+buffer.capacity());

        //放入数据到缓冲区
        String str="abcde";
        buffer.put(str.getBytes());
        System.out.println("=============put==============");
        System.out.println("position:"+buffer.position());
        System.out.println("limit:"+buffer.limit());
        System.out.println("capacity:"+buffer.capacity());

        //切换缓冲区为读模式
        buffer.flip();
        System.out.println("=============flip==============");
        System.out.println("position:"+buffer.position());
        System.out.println("limit:"+buffer.limit());
        System.out.println("capacity:"+buffer.capacity());

        //获取缓冲区数据
        byte[] dst=new byte[buffer.limit()];
        buffer.get(dst,0,2);
        System.out.println(new String(dst));
        System.out.println("=============get==============");
        System.out.println("position:"+buffer.position());
        System.out.println("limit:"+buffer.limit());
        System.out.println("capacity:"+buffer.capacity());

        //make记录position位置
        buffer.mark();
        byte bge=buffer.get();
        System.out.println(bge);
        System.out.println("=============make==============");
        System.out.println("position:"+buffer.position());
        System.out.println("limit:"+buffer.limit());
        System.out.println("capacity:"+buffer.capacity());

        //reset恢复make记录的position位置
        buffer.reset();
        System.out.println("=============reset==============");
        System.out.println("position:"+buffer.position());
        System.out.println("limit:"+buffer.limit());
        System.out.println("capacity:"+buffer.capacity());

    }
}
