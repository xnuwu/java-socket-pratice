package com.jnet.nio;

import java.io.IOException;
import java.net.Socket;

/**
 * @author: yangxunwu
 * @date: 2020/12/10 11:06
 */
public class BlockSocketChannel {
    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 2020);
        socket.getOutputStream().write("hello\r\n".getBytes());
        socket.getOutputStream().write("bye".getBytes());
        socket.close();
    }
}
