package com.jnet.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: yangxunwu
 * @date: 2020/12/10 11:06
 */
public class NoBlockThreadSocketChannel {

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 20, Integer.MAX_VALUE, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());

        for(int i = 0; i < 10; i++) {
            poolExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    sendMessage();
                }
            });
        }

        poolExecutor.shutdown();

        while (!poolExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
//            System.out.println("wait thread end " + System.currentTimeMillis());
        }
    }

    public static void sendMessage() {
        try{
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

                        if(data != null && data.trim().length() > 0) {
                            System.out.println(data);
                        }
                    }

                    if(selectionKey.isWritable()) {
                        MySocketChannelUtil.write(socketChannel, (Thread.currentThread() + " hello " + (System.currentTimeMillis()) +"\n").getBytes());
                    }
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
