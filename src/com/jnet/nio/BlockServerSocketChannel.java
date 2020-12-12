package com.jnet.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.*;

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
            try{

                PrintWriter writer = getWriter(socket);
                BufferedReader reader = getReader(socket);

                String message = null;
                while ((message = reader.readLine()) != null) {
                    System.out.println("receive: " + message);

                    if("bye".equals(message)) {
                        writer.write("bye client!");
                        writer.flush();
                        break;
                    }
                }

            }catch (IOException e) {
               e.printStackTrace();
            }finally {
                if(socketChannel != null) {
                    try {
                        socketChannel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public PrintWriter getWriter(Socket socket) throws IOException {
            return new PrintWriter(socket.getOutputStream());
        }

        public BufferedReader getReader(Socket socket) throws IOException {
            return new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
    }

    public static void main(String[] args) throws IOException {
        BlockServerSocketChannel blockServerSocketChannel = new BlockServerSocketChannel();
        blockServerSocketChannel.service();
    }
}

