package com.jnet.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author: yangxunwu
 * @date: 2020/12/10 11:06
 */
public class NoBlockSocketChannel {

    public static void main(String[] args) throws IOException, InterruptedException {

        SocketChannel socketChannel =  SocketChannel.open(new InetSocketAddress("127.0.0.1", 2020));
        socketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        while (selector.select() > 0) {

            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                keyIterator.remove();

                if(selectionKey.isReadable()) {
                    String data = MySocketChannelUtil.readLine(socketChannel);

                    if(data != null) {
                        System.out.println("receive " + data);
                    }
                }

                if(selectionKey.isWritable()) {
                    MySocketChannelUtil.write(socketChannel, ("hello " + (System.currentTimeMillis()) +"\n").getBytes());
                }
            }
        }
    }
}
