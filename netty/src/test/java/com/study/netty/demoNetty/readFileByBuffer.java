package com.study.netty.demoNetty;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author 冯云伟
 * @date 2021/10/24
 */
public class readFileByBuffer {
    public static void main(String[] args) {

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
}
