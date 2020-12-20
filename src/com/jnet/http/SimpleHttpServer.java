package com.jnet.http;

import com.jnet.util.ByteBufferCodec;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * mini http server implementation
 * @author Xunwu Yang 2020-12-20
 * @version 1.0.0
 */
public class SimpleHttpServer {

    private static final int port = 80;
    private ServerSocketChannel serverSocketChannel;
    private ExecutorService executorService;
    private static final int THREAD_POOL_MULTIPLE = 4;

    public static void main(String[] args) throws IOException {
        new SimpleHttpServer().service();
    }

    public SimpleHttpServer() throws IOException {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * THREAD_POOL_MULTIPLE);
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().setReuseAddress(true);
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        System.out.println("http server started!");
    }

    public void service() {
        while (true) {
            SocketChannel socketChannel = null;
            try{
                socketChannel = serverSocketChannel.accept();
                executorService.execute(new Handler(socketChannel));
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class Handler implements Runnable {

        private SocketChannel socketChannel;

        private Handler(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            handle(socketChannel);
        }

        private void handle(SocketChannel socketChannel) {

            try{
                Socket socket = socketChannel.socket();
                System.out.println("receive connected from " + socket.getInetAddress().getHostName() + ":" + socket.getPort());

                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                socketChannel.read(byteBuffer);
                byteBuffer.flip();

                String message = ByteBufferCodec.decode(byteBuffer);
                System.out.println(message);

                StringBuffer response = new StringBuffer("HTTP/1.1 200 OK\r\n");
                response.append("Content-Type:text/html\r\n\r\n");
                socketChannel.write(ByteBufferCodec.encode(response.toString()));

                FileInputStream fileInputStream;

                String firstLine = message.substring(0, message.indexOf("\r\n"));
                if(firstLine.contains("login.html")) {
                    fileInputStream = new FileInputStream("D:\\temp\\jnet\\src\\com\\jnet\\http\\login.html");
                }else{
                    fileInputStream = new FileInputStream("D:\\temp\\jnet\\src\\com\\jnet\\http\\hello.html");
                }

                FileChannel fileChannel = fileInputStream.getChannel();
                System.out.println("response " + fileChannel.size() + " bytes");
                fileChannel.transferTo(0, fileChannel.size(), socketChannel);
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
    }
}
