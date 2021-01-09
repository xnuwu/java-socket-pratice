package com.jnet.serialize;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Xunwu Yang 2021-01-09
 * @version 1.0.0
 */
public class SimpleClient {
    public static void main(String[] args) {
        while (true) {
            try{
                Socket socket = new Socket(InetAddress.getLocalHost(), 8000);
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Object obj1 = objectInputStream.readObject();
                Object obj2 = objectInputStream.readObject();
                System.out.println("obj1:" + obj1);
                System.out.println("obj2:" + obj2);
                System.out.println("obj1 == obj2 " + (obj1 == obj2));
            }catch (IOException e) {

            } catch (ClassNotFoundException e) {

            }
        }
    }
}
