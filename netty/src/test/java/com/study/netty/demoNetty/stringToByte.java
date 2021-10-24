package com.study.netty.demoNetty;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * String 转ByteBuffer  &&分散读取
 *
 * @author 冯云伟
 */
public class stringToByte {
    public static void main(String[] args) {
         //1.字符串转为ByteBuffer  未切换到读模式需要手动切换再读
        ByteBuffer buffer=ByteBuffer.allocate(16);
        buffer.put("hello".getBytes());


        //2.charset  已经切换到读模式  可以直接读
        ByteBuffer buffer1= StandardCharsets.UTF_8.encode("hello,word");

        //3.wrap   已经切换到读模式  可以直接读
        ByteBuffer buffer2=ByteBuffer.wrap("hello,word".getBytes());

        //byteBuffer to String
        String s = StandardCharsets.UTF_8.decode(buffer2).toString();
        System.out.println(s);

        //分散读取
/*        try (FileChannel channel = new FileInputStream("words.txt").getChannel()) {
            //方法一
            ByteBuffer b1 = ByteBuffer.allocate(3);
            ByteBuffer b2 = ByteBuffer.allocate(3);
            ByteBuffer b3 = ByteBuffer.allocate(5);
            channel.read(new ByteBuffer[]{b1, b2, b3});
            b1.flip();
            b2.flip();
            b3.flip();
            System.out.println(StandardCharsets.UTF_8.decode(b1).toString());
            System.out.println(StandardCharsets.UTF_8.decode(b2).toString());
            System.out.println(StandardCharsets.UTF_8.decode(b3).toString());
            //todo 方法二  没弄懂为什么不行
 *//*          ByteBuffer buffer = ByteBuffer.allocate(30);
            channel.read(buffer);
            ByteBuffer buffer1 = buffer.get(new byte[3]);
            ByteBuffer buffer2 = buffer.get(new byte[3]);
            ByteBuffer buffer3 = buffer.get(new byte[5]);
            buffer1.flip();
            buffer2.flip();
            buffer3.flip();
            System.out.println(StandardCharsets.UTF_8.decode(buffer1).toString());
            System.out.println(StandardCharsets.UTF_8.decode(buffer2).toString());
            System.out.println(StandardCharsets.UTF_8.decode(buffer3).toString());*//*
        } catch (IOException e) {
        }*/

    }
}
