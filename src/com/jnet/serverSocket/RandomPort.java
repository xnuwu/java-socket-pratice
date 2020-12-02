package com.jnet.serverSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

//TODO a simple ftp server & client
public class RandomPort {

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(10, 20, Integer.MAX_VALUE, TimeUnit.MINUTES, new LinkedBlockingQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());

        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    new Server(20).service();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        poolExecutor.execute(new Runnable() {
            @Override
            public void run() {
                new Client("127.0.0.1", 20).connect();
            }
        });

        poolExecutor.shutdown();

        while (!poolExecutor.awaitTermination(1, TimeUnit.SECONDS)) {
        }
    }

    static class Client {

        private String host;
        private Integer port;

        private Socket socket;
        private ServerSocket randomPortServerSocket;

        public Client(String host, Integer port) {
            this.host = host;
            this.port = port;
        }

        public void connect() {
            try{
                socket = new Socket(host, port);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream());

                String message = "";
                while ((message = reader.readLine()) != null) {
                    System.out.println("client receive: " + message);
                    randomPortServerSocket = new ServerSocket(0);
                    System.out.println("client create a random socket at " + randomPortServerSocket.getLocalPort() + " port!");
                    writer.println("random socket is " + randomPortServerSocket.getLocalPort());
                    writer.flush();
                    break;
                }

                System.out.println("random server socket wait client!");
                Socket randomPortSocket = randomPortServerSocket.accept();
                System.out.println("random server socket get a client connect!");
                BufferedReader randomReader = new BufferedReader(new InputStreamReader(randomPortSocket.getInputStream()));
                PrintWriter randomWriter = new PrintWriter(randomPortSocket.getOutputStream());

                String randomMessage = "";
                while ((randomMessage = randomReader.readLine()) != null) {
                    System.out.println("receive message: " + message);
                    randomWriter.write("random received message: " + randomMessage);
                    randomWriter.flush();
                }
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

                if(randomPortServerSocket != null) {
                    try {
                        randomPortServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class Server {

        private Integer port;
        private ServerSocket serverSocket;

        public Server(Integer port) throws IOException {
            this.port = port;
            this.serverSocket = new ServerSocket(port);
            System.out.println("服务启动!");
        }

        public void service() {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                System.out.println("Server receive a client connect!");
                PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
                printWriter.println("Hello client, I'm Server!");
                printWriter.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message = null;

                while ((message = reader.readLine()) != null) {
                    System.out.println("receive client message: " + message);
                    String[] params = message.split("\\s");
                    Integer port = Integer.parseInt(params[params.length - 1]);
                    System.out.println("try connect to client server " + socket.getInetAddress() + ":" + port);
                    Socket clientSocket = new Socket(socket.getInetAddress(), port);
                    System.out.println("server's client port " + clientSocket.getLocalPort());
                    PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                    writer.println("hello client server");
                    writer.flush();
                    break;
                }

            } catch (IOException e) {
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
