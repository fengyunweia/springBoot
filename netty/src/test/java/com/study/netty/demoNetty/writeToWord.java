package com.study.netty.demoNetty;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

public class writeToWord {
    public static void main(String[] args) {
        ByteBuffer b1= StandardCharsets.UTF_8.encode("hello");
        ByteBuffer b2= StandardCharsets.UTF_8.encode("my");
        ByteBuffer b3= StandardCharsets.UTF_8.encode("word");
        try (FileChannel channel = new FileOutputStream("write.txt").getChannel()) {
            channel.write(new ByteBuffer[]{b1,b2,b3});
        } catch (IOException e) {
        }

    }
}
