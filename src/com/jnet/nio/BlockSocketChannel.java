package com.jnet.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @author: yangxunwu
 * @date: 2020/12/10 11:06
 */
public class BlockSocketChannel {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 20);
        socket.getOutputStream().write("hello\r\n".getBytes());
        socket.getOutputStream().write("bye\r\n".getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String message = null;
        while ((message = reader.readLine()) != null) {
            System.out.println("client:" + message);
        }
        Thread.sleep(100000);
        socket.close();
    }
}
