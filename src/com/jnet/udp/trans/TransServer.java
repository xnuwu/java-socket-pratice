package com.jnet.udp.trans;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author Xunwu Yang 2020-12-31
 * @version 1.0.0
 */
public class TransServer {

    private static final int port = 8000;
    private DatagramSocket socket;

    public TransServer() throws SocketException {
        socket = new DatagramSocket(port);
        socket.setReuseAddress(true);
        System.out.println("trans server listen: 8000/udp");
    }

    public void service() throws IOException {
        while (true) {
            DatagramPacket data = new DatagramPacket(new byte[1024], 1024);
            socket.receive(data);
            long longData = byteToLong(data.getData());
            System.out.println("data is " + longData);
        }
    }

    public long byteToLong(byte[] data) throws IOException {
        ByteArrayInputStream bai = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bai);
        return dis.readLong();
    }

    public static void main(String[] args) throws IOException {
        new TransServer().service();
    }
}
