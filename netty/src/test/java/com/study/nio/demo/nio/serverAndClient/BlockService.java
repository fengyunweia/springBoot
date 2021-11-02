package com.study.nio.demo.nio.serverAndClient;

import com.study.nio.ByteBufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;


public class BlockService {
    private static final Logger log=LoggerFactory.getLogger(BlockService.class);
    public static void main(String[] args) throws IOException {
        //阻塞模式
        block();
        //非阻塞模式
//        unBlock();
    }

    private static void unBlock() throws IOException {
        //创建服务器
        ServerSocketChannel server=ServerSocketChannel.open();
        //配置非阻塞模式
        server.configureBlocking(false);

        ByteBuffer buffer=ByteBuffer.allocate(16);
        //绑定监听端口
        server.bind(new InetSocketAddress(8888));
        List<SocketChannel> list=new ArrayList<>();
        while(true) {
            //accept建立与客户端的连接 socketChannel 用来与客户端通信
            SocketChannel socketChannel = server.accept(); //这一步不会阻塞 如果没有连接，线程还是继续运行，返回null
            if(socketChannel!=null) {
                log.debug("connected..." + socketChannel);
                socketChannel.configureBlocking(false);
                //将建立的连接都放到一个list里面  然后读取每个连接传送的数据
                list.add(socketChannel);
            }
            for (SocketChannel channel : list) {
                int read = channel.read(buffer);//非阻塞 线程继续运行 没发返回0
                if(read>0) {
                    buffer.flip();
                    ByteBufferUtil.debugAll(buffer);
                    buffer.clear();
                    log.debug("after read...." + channel);
                }
            }
        }

    }

    private static void block() throws IOException {
        //创建服务器
        ServerSocketChannel server=ServerSocketChannel.open();

        ByteBuffer buffer=ByteBuffer.allocate(16);
        //绑定监听端口
        server.bind(new InetSocketAddress(8888));
        List<SocketChannel> list=new ArrayList<>();
        while(true) {
            log.debug("connecting....");
            //accept建立与客户端的连接 socketChannel 用来与客户端通信
            SocketChannel socketChannel = server.accept();
            log.debug("connected..."+socketChannel);
            //将建立的连接都放到一个list里面  然后读取每个连接传送的数据
            list.add(socketChannel);
            for (SocketChannel channel : list) {
                log.debug("before read...." + channel);
                channel.read(buffer);
                buffer.flip();
                ByteBufferUtil.debugAll(buffer);
                buffer.clear();
                log.debug("after read...." + channel);
            }
        }
    }
}
