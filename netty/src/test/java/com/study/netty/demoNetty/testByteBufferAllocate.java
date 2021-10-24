package com.study.netty.demoNetty;

import javax.swing.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class testByteBufferAllocate {
    public static void main(String[] args) {
        System.out.println(ByteBuffer.allocate(10).getClass());
        System.out.println(ByteBuffer.allocateDirect(10).getClass());

        /**
         * class java.nio.HeapByteBuffer   - java 堆内存 读写效率低 收到 GC(垃圾回收)的影响
         *                                  why：写入时  当堆内存不够的时候，会触发GC 内存碎片整理，把垃圾碎片从一个地方搬迁其他地方（拷贝）
         * class java.nio.DirectByteBuffer  -直接内存（系统内存） 读写效率高（少一次拷贝） 不手GC影响 缺点：分配内存效率低
         */
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.put(new byte[]{'a', 'b', 'c', 'd'});
        buffer.flip();
/*        buffer.get(new byte[2]);
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get(0));*/

        //mark & reset
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        buffer.mark(); //在索引2 的位置加标记
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        buffer.reset();//本来缓存中的数据已经读完了，但是因为加了标记，reset后还能读标记下面的数据


    }
}
