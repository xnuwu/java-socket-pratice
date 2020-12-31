package com.jnet.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * @author Xunwu Yang 2020-12-31
 * @version 1.0.0
 */
public class ConnectTest {

    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();

        //绑定当时UDP到指定端口，只接受发送指定“连接"的数据
        socket.connect(new InetSocketAddress("127.0.0.1", 1001));

        //报错：connected address and packet address differ
        socket.send(new DatagramPacket(new byte[1024], 1024, new InetSocketAddress("127.0.0.1", 1002)));
    }
}
