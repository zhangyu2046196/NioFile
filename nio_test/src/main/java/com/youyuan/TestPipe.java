package com.youyuan;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Pipe;

/**
 * @author zhangyu
 * @version 1.0
 * @description Pipe管道测试类信息
 * 管道是两个线程之间的数据连接，一个管道中有两个通道sink通道和source通道
 * 给sink通道写数据，从source通道读数据
 * @date 2019/3/15 14:21
 */
public class TestPipe {

    @Test
    public void test() throws IOException {
        //创建管道
        Pipe pipe = Pipe.open();
        //获取sink通道
        Pipe.SinkChannel sink = pipe.sink();
        //创建缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put("北京友缘在线网络科技股份有限公司".getBytes());
        buf.flip();
        sink.write(buf);

        //获取source通道
        Pipe.SourceChannel source = pipe.source();
        source.read(buf);
        buf.flip();
        System.out.println(new String(buf.array()));

        //关闭连接
        source.close();
        sink.close();
    }

}
