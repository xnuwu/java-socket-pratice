package com.jnet.serialize;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @author Xunwu Yang 2021-01-09
 * @version 1.0.0
 */
public class SimpleServer {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String objectType;
        while ((objectType = bufferedReader.readLine()) != null) {
            Object object;
            switch (objectType) {
                case "string":
                    object = "hello " + System.currentTimeMillis();
                    break;

                case "date":
                    object = new Date();
                    break;

                case "int":
                    object = 10;
                    break;

                case "customer":
                    object = new Customer("xunwu", 100);
                    break;

                default:
                    object = System.currentTimeMillis();
                    break;
            }
            System.out.println("prepare send object " + object);
            new SimpleServer().send(object);
        }
    }

    public void send(Object object) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);
        Socket socket = serverSocket.accept();

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject(object);
        objectOutputStream.writeObject(object);
        objectOutputStream.close();
        socket.close();
        serverSocket.close();
        System.out.println("already send " + object);
    }
}
