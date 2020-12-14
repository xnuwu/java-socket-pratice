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
import java.util.Set;

public class NoBlockServerSocketChannel {

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private Charset charset = Charset.forName("UTF-8");

    public NoBlockServerSocketChannel() throws IOException {
        selector = Selector.open();
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().setReuseAddress(true);
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(20));
    }

    public void service() throws IOException {

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("no block server started!");
        while (selector.select() > 0) {
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = null;

                try{
                    selectionKey = keyIterator.next();
                    keyIterator.remove();

                    if(selectionKey.isAcceptable()) {

                        ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = ssc.accept();
                        System.out.println("connected from " + socketChannel.socket().getInetAddress() + ":" + socketChannel.socket().getPort());
                        socketChannel.configureBlocking(false);

                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, byteBuffer);
                    }

                    if(selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();

                        ByteBuffer readBuffer = ByteBuffer.allocate(32);
                        socketChannel.read(readBuffer);
                        readBuffer.flip();

                        buffer.limit(buffer.capacity());
                        buffer.put(readBuffer);
                    }

                    if(selectionKey.isWritable()) {

                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();

                        buffer.flip();
                        String rawData = decode(buffer);

                        if(rawData.contains("\r\n")) {

                            String data = rawData.substring(0, rawData.indexOf("\n") + 1);
                            String ret = "hello, server receive " + data.length() + " bytes! " + data;
                            System.out.println(ret);
                            write(socketChannel, ret.getBytes());

                            ByteBuffer temp = encode(data);

                            buffer.position(temp.limit());
                            buffer.compact();

                            if("bye".equals(data.trim())) {
                                System.out.println("receive bye! close key and channel!");
                                selectionKey.cancel();
                                socketChannel.close();
                                break;
                            }

                        }else{
                            return;
                        }
                    }

                }catch (IOException e) {
                    e.printStackTrace();
                    if(selectionKey != null) {
                        selectionKey.cancel();
                    }
                    selectionKey.channel().close();
                }
            }
        }
    }

    private void write(SocketChannel socketChannel, byte[] data) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.put(data);

        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
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
        NoBlockServerSocketChannel noBlockServerSocketChannel = new NoBlockServerSocketChannel();
        noBlockServerSocketChannel.service();
    }
}