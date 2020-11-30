package com.jnet.socket;

import java.io.IOException;
import java.net.ServerSocket;

public class SimpleServer {
    public static void main(String[] args) throws InterruptedException, IOException {
        ServerSocket serverSocket = new ServerSocket(20, 1);
        Thread.sleep(60 * 3 * 1000);
    }
}
