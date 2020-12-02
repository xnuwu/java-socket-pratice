package com.jnet.serverSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Backlog {

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 20, Integer.MAX_VALUE, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());

        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    new Server(20, 300).service();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    new Client("127.0.0.1", 20).connect();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        poolExecutor.shutdown();

        while (!poolExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
        }
    }

    static class Client {

        private String host;
        private Integer port;

        public Client(String host, Integer port) {
            this.host = host;
            this.port = port;
        }

        public void connect() throws IOException, InterruptedException {
            Socket[] sockets = new Socket[2024];

            for(int i = 0; i < 2024; i++) {
                sockets[i] = new Socket(host, port);
                System.out.println("第" + i + "个连接，建立成功!本地端口: " + sockets[i].getLocalPort());
            }

            for(int i = 0; i < 2024; i++) {
                sockets[i].close();
                System.out.println("第" + i + "个连接关闭!");
            }
        }
    }

    static class Server {

        private Integer port;
        private Integer backlog;
        private ServerSocket serverSocket;

        public Server(Integer port, Integer backlog) throws IOException {
            this.port = port;
            this.backlog = backlog;
            this.serverSocket = new ServerSocket(port, backlog);
            System.out.println("服务启动!");
        }

        public void service() {
            while (true) {
                Socket socket = null;
                try{
                    socket = serverSocket.accept();
                    InetAddress clientAddress = socket.getInetAddress();
                    System.out.println("client " + clientAddress + "connected at port" + socket.getPort());
                    Thread.sleep(1000);
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
