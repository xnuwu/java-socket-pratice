package com.jnet.serverSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadSocketServer {

    public volatile static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(20, 10000);

        monitorClient();

        while (true) {
            Socket socket = serverSocket.accept();
            Thread workThread = new Thread(new Handler(socket));

            //java.lang.OutOfMemoryError: unable to create new native thread
            workThread.start();
        }
    }

    private static void monitorClient() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Socket socket = null;
                    while (true) {
                        try {
                            socket = new Socket("127.0.0.1", 20);
                            PrintWriter writer = new PrintWriter(socket.getOutputStream());
                            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                            writer.println("what time now is ?");
                            writer.flush();

                            String message = reader.readLine();
                            System.out.println("message: " + message);

                            Random random = new Random();
                            Thread.sleep(random.nextInt(1000));

                            writer.println("bye");
                            writer.flush();

                            writer.close();
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
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
            }).start();
        }
    }
}

class Handler implements Runnable {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public Handler(Socket socket) throws IOException {
        this.socket = socket;
        this.writer = getWriter();
        this.reader = getReader();
    }

    @Override
    public void run() {
        String message = null;
        try{
            while ((message = reader.readLine()) != null) {
                if("bye".equals(message)) {
                    break;
                }else{
                    writer.println(echo());
                    writer.flush();
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {

            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            writer.flush();
            writer.close();
        }
    }

    private String echo() {
        String ret;
        synchronized (ThreadSocketServer.counter) {
            ret = "server " + ThreadSocketServer.counter.incrementAndGet() + " time is " + System.currentTimeMillis();
        }
        return ret;
    }

    public PrintWriter getWriter() throws IOException {
        return new PrintWriter(socket.getOutputStream());
    }

    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }
}
