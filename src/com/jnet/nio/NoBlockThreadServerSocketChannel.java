package com.jnet.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class NoBlockThreadServerSocketChannel {

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private Charset charset = Charset.forName("UTF-8");

    private final Object lock = new Object();

    public NoBlockThreadServerSocketChannel() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().setReuseAddress(true);
        serverSocketChannel.socket().bind(new InetSocketAddress(2020));
    }

    public void accept() {
        while (true) {
            try{
                SocketChannel socketChannel = serverSocketChannel.accept();
                socketChannel.configureBlocking(false);

                synchronized (lock) {
                    selector.wakeup();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, buffer);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void service() throws IOException {

        System.out.println("======= NIO Thread Server Started! =======");

        while (true) {

            synchronized (lock) {}
            int readyNum = selector.select();

            if(readyNum == 0) {
                continue;
            }

            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = null;

                try{
                    selectionKey = keyIterator.next();
                    keyIterator.remove();

                    if(selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();

                        ByteBuffer readBuffer = ByteBuffer.allocate(32);
                        int readNum = socketChannel.read(readBuffer);
                        readBuffer.flip();

                        buffer.limit(buffer.capacity());

                        //if size not enough, discard it
                        if(buffer.hasRemaining() && buffer.remaining() >= readNum) {
                            buffer.put(readBuffer);
                        }
                    }

                    if(selectionKey.isWritable()) {

                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();

                        buffer.flip();
                        String rawData = decode(buffer);

                        if(rawData.contains("\n")) {

                            String data = rawData.substring(0, rawData.indexOf("\n") + 1);
                            String ret = "hello, server receive " + data.length() + " bytes! " + data + "\n";
                            System.out.println(ret);
                            MySocketChannelUtil.write(socketChannel, ret.getBytes());

                            ByteBuffer temp = encode(data);

                            buffer.position(temp.limit());
                            buffer.compact();

                            if("bye".equals(data.trim())) {
                                System.out.println("receive bye! close key and channel!");
                                selectionKey.cancel();
                                socketChannel.close();
                                break;
                            }
                        }
                    }

                }catch (IOException e) {
                    e.printStackTrace();
                    selectionKey.cancel();
                    selectionKey.channel().close();
                }
            }
        }
    }

    private String decode(ByteBuffer buffer) {
        CharBuffer charBuffer = charset.decode(buffer);
        return charBuffer.toString();
    }

    private ByteBuffer encode(String data) {
        return charset.encode(data);
    }

    public static void main(String[] args) throws IOException {
        NoBlockThreadServerSocketChannel noBlockServerSocketChannel = new NoBlockThreadServerSocketChannel();

        new Thread(new Runnable() {
            @Override
            public void run() {
                noBlockServerSocketChannel.accept();
            }
        }).start();

        noBlockServerSocketChannel.service();
        System.out.println("service done!");
    }
}