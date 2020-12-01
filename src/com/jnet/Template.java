package com.jnet;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: yangxunwu
 * @date: 2020/12/1 12:21
 */
public class Template {

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 20, Integer.MAX_VALUE, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());

        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });

        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {

            }
        });

        poolExecutor.shutdown();

        while (!poolExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
//            System.out.println("wait thread end " + System.currentTimeMillis());
        }

    }
}

class TemplateServer {

    private int port;
    private ServerSocket serverSocket;

    public TemplateServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    public void receive() {

    }
}

class TemplateClient {

    private String host;
    private int port;
    private Socket socket;

    public TemplateClient(String host, int port) {
        this.host = host;
        this.port = port;
        this.socket = new Socket();
    }

    public void send() {

    }
}