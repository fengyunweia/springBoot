package com.study.io.demo.nio;

import com.study.io.ByteBufferUtil;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ByteBufferExam {
    public static void main(String[] args) {
        /*
         * 网络上有多条数据发送给服务器 数据之间用 \n进行分割
         * 但是因为某种原因接受时，被重新组合，例如原始数据有3条
         *     hello，world\n
         *      I'm zhangshan\n
         *       How are you?\n
         *     变成了下面两个byteBuffer (粘包，半包)
         *     hello,word\nI'm zhangshan\nHo
         *      w are you?\n
         * 编程回复
         * */
        ByteBuffer source = ByteBuffer.allocate(60);
        source.put("hello,word\nI'm zhangsan\nHo".getBytes());
        split(source);
        source.put("w are you?\n".getBytes());
        split(source);


    }

    private static void split(ByteBuffer source) {
        source.flip();
        for (int i = 0; i < source.limit(); i++) {
            if (source.get(i) == '\n') {
                //存入一个新的ByteBuffer
                int length = i + 1 - source.position();
                ByteBuffer byteBuffer = ByteBuffer.allocate(length);
                //读到byteBuffer中
                for (int j = 0; j < length; j++) {
                    byteBuffer.put(source.get());
                }
                ByteBufferUtil.debugAll(byteBuffer);
                write(byteBuffer);
            }
        }
        source.compact();
    }

    private static void write(ByteBuffer byteBuffer) {
        try (FileChannel channel = new FileOutputStream("write.txt").getChannel()) {
            int write = channel.write(byteBuffer);
            System.out.println(write);
        } catch (IOException e) {
        }
    }
}
