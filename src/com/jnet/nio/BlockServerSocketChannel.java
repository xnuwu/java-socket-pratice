package com.jnet.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: yangxunwu
 * @date: 2020/12/10 11:06
 */
public class BlockServerSocketChannel {

    private ThreadPoolExecutor executor;
    private ServerSocketChannel serverSocketChannel;

    public BlockServerSocketChannel() throws IOException {

        executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 2,
                10,
                TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy());


        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().setReuseAddress(true);
        serverSocketChannel.socket().bind(new InetSocketAddress(2020));
        System.out.println("BlockServerSocketChannel is running!");
    }

    public void service() throws IOException {
        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();
            executor.execute(new Worker(socketChannel));
        }
    }

    class Worker implements Runnable {

        private SocketChannel socketChannel;

        public Worker(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            Socket socket = socketChannel.socket();
            System.out.println("connected from " + socket.getInetAddress() + ":" + socket.getPort());
            try {


                String message;
                while ((message = readLine(socketChannel)) != null) {
                    System.out.println("receive: " + message);

                    if ("bye".equals(message)) {
                        write(socketChannel, "bye client!".getBytes());
                        System.out.println("received bye! closing connect!");
                        break;
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socketChannel != null) {
                    try {
                        socketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static String readLine(SocketChannel socketChannel) throws IOException {

        //assume line's length less than 1024 byte
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ByteBuffer tempBuffer = ByteBuffer.allocate(1);

        int readSize;
        String data = null;

        while (true) {
            tempBuffer.clear();
            readSize = socketChannel.read(tempBuffer);

            if (readSize == -1) {
                break;
            }

            if (readSize == 0) {
                continue;
            }

            tempBuffer.flip();
            buffer.put(tempBuffer);

            buffer.flip();
            Charset utf8 = Charset.forName("UTF-8");
            CharBuffer charBuffer = utf8.decode(buffer);
            data = charBuffer.toString();

            if (data.contains("\r\n")) {
                data = data.substring(0, data.indexOf("\r\n"));
                break;
            }

            buffer.position(buffer.limit());
            buffer.limit(buffer.capacity());
        }

        return data;
    }

    private static void write(SocketChannel socketChannel, byte[] data) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(data.length);
        buffer.put(data);

        while (buffer.hasRemaining()) {
            socketChannel.write(buffer);
        }
    }

    public static void main(String[] args) throws IOException {
        BlockServerSocketChannel blockServerSocketChannel = new BlockServerSocketChannel();
        blockServerSocketChannel.service();
    }
}

