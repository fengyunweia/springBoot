package com.study.io.demo.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class BaseNio {
    public static void main(String[] args) {

        writeToFile();

        channelToChannel();

        StringToByte();

        readFileByBuffer();

        markAndReset();


    }

    private static void markAndReset() {
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

        //mark & reset
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        buffer.mark(); //在索引2 的位置加标记
        System.out.println((char) buffer.get());
        System.out.println((char) buffer.get());
        buffer.reset();//本来缓存中的数据已经读完了，但是因为加了标记，reset后还能读标记下面的数据
    }

    private static void readFileByBuffer() {
        //将文件中的数据读入到双向通道channel中
        try (FileChannel channel = new FileInputStream("data.txt").getChannel()) {
            //创建一个10个字节大小的缓存buffer
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (true) {
                //如果int=-1 则表示读不到内容了
                int read = channel.read(buffer);
                if (read == -1) {
                    break;
                }
                /**
                 * 切换到只读模式  why: byteBuffer有三个参数，position 写入位置  limit 缓存内数据大小 capacity 创建的byteBuffer大小
                 * 写模式 limit==capacity position=0 每次写一个数据往后诺一个位置，
                 * 切换到读模式，则把limit放到position结束位置 position放到数据开始位置，从头开始读
                 */
                buffer.flip();
                while (buffer.hasRemaining()) {
                    System.out.println((char) buffer.get());
                }
                /**
                 * 读完了，却换到写模式，继续写
                 *
                 * 附：buffer.compact()方法是把当前没读完的数据写道新缓存的开始，然后写新数据
                 */
                buffer.clear();

            }
        } catch (IOException e) {
        }
    }

    private static void writeToFile() {
        ByteBuffer b1= StandardCharsets.UTF_8.encode("hello");
        ByteBuffer b2= StandardCharsets.UTF_8.encode("my");
        ByteBuffer b3= StandardCharsets.UTF_8.encode("word");
        try (FileChannel channel = new FileOutputStream("write.txt").getChannel()) {
            long write = channel.write(new ByteBuffer[]{b1, b2, b3});
            System.out.println(write);
        } catch (IOException e) {
        }
    }

    private static void channelToChannel() {
        try (FileChannel read = new FileInputStream("data.txt").getChannel();
             FileChannel write = new FileOutputStream("words.txt").getChannel()) {
            read.transferTo(write.size(), read.size(), write);
        } catch (IOException e) {
        }
    }

    private static void StringToByte() {
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
