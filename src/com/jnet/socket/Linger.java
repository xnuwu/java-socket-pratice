package com.jnet.socket;

/**
 * @author: yangxunwu
 * @date: 2020/12/1 12:20
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: yangxunwu
 * @date: 2020/12/1 12:21
 */
public class Linger {

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 20, Integer.MAX_VALUE, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());

        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    new LingerServer(20).receive();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    new LingerClient("127.0.0.1", 20).send();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        poolExecutor.shutdown();

        while (!poolExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
//            System.out.println("wait thread end " + System.currentTimeMillis());
        }

    }
}

class LingerServer {

    private int port;
    private ServerSocket serverSocket;

    public LingerServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    public void receive() throws IOException, InterruptedException {
        Socket socket = serverSocket.accept();
        Thread.sleep(1000);
        InputStream inputStream = socket.getInputStream();
        int total = 0;
        int len;
        byte[] buff = new byte[1024];

        while ((len = inputStream.read(buff)) != -1) {
            total += len;
        }

        socket.close();
        serverSocket.close();

        System.out.println("total read " + total + " byte");
    }
}

class LingerClient {

    private String host;
    private int port;
    private Socket socket;

    public LingerClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.socket = new Socket();
    }

    public void send() throws IOException {

        this.socket.setSoLinger(true, 1);
        this.socket.connect(new InetSocketAddress(host, port));
        OutputStream os = socket.getOutputStream();
        socket.sendUrgentData(1);
        for(int i = 0; i < 100000; i++) {
            os.write("hello".getBytes());
        }
        System.out.println("close socket");
        Long beginTime = System.currentTimeMillis();
        socket.close();
        Long endTime = System.currentTimeMillis();
        System.out.println("close time " + (endTime - beginTime) + "ms");
    }
}