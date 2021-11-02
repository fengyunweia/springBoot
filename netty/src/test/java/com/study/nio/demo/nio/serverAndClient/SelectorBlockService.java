package com.study.nio.demo.nio.serverAndClient;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;

import com.study.nio.ByteBufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;

public class SelectorBlockService {
    private static final Logger log=LoggerFactory.getLogger(BlockService.class);
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        ByteBuffer buffer=ByteBuffer.allocate(16);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        //建立selector与channel的练气
        //selectionKey 事件发生后，可以知道事件是那个channel的时间
        SelectionKey sscKey = ssc.register(selector, 0, null);
        sscKey.interestOps(SelectionKey.OP_ACCEPT);
        ssc.bind(new InetSocketAddress(8888));
        while(true) {
            //selector方法 没有事件发生，线程阻塞，有事件发生 恢复运行
            log.debug("connecting....");

            //事件不处理的情况，此方法会觉的上一个事件未结束，一直执行，陷入阻塞
            // 可以使用channel.cancel()方法取消
            selector.select();
            //处理事件  selectedKeys包含了所有发生的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey selectionKey = iterator.next();
                ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                SocketChannel accept = channel.accept();
                accept.write(buffer);
                ByteBufferUtil.debugAll(buffer);
                log.debug("{}",accept);

            }
        }
    }
}

