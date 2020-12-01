package com.jnet.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * @author: yangxunwu
 * @date: 2020/12/1 11:35
 */
public class Timeout {
    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new ReceiveServer(20).receive();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    new SendServer("127.0.0.1", 20).send();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Thread.sleep(5 * 10000);
    }
}

class SendServer {

    private String host;
    private int port;
    private Socket socket;

    public SendServer(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.socket = new Socket(this.host, this.port);
    }


    public void send() throws IOException, InterruptedException {
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("hello, world ".getBytes());
        outputStream.write("everyone".getBytes());
        outputStream.flush();
        Thread.sleep(3 * 1000);
        socket.close();
    }
}

class ReceiveServer {

    private int port;
    private ServerSocket serverSocket;

    public ReceiveServer(int port) throws IOException {
        this.port = port;
        this.serverSocket = new ServerSocket(port);
    }

    public void receive() throws IOException {
        Socket socket = serverSocket.accept();

        socket.setSoTimeout(1000);
        int readLength;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        do {
           try{
               byte[] buff = new byte[1024];
               InputStream is = socket.getInputStream();
               readLength = is.read(buff);
               if(readLength != -1) {
                   buffer.write(buff, 0, readLength);
               }
           }catch (SocketTimeoutException e) {
               //socket will keep connected
               System.out.println("read timeout, " + e.getMessage());
               readLength = 0;
           }

        }while (readLength != -1);

        System.out.println("read data: " + buffer.toString());
    }
}