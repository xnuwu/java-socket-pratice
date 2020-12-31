package com.jnet.udp.trans;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * @author Xunwu Yang 2020-12-31
 * @version 1.0.0
 */
public class TransClient {

    private String host = "localhost";
    private int port = 8000;
    private DatagramPacket packet;

    public TransClient() {
        packet = new DatagramPacket(new byte[8], 8, new InetSocketAddress(host, port));
    }

    public void talk() throws IOException {
        String data;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket socket = new DatagramSocket();
        while ((data = bufferedReader.readLine()) != null) {
            long longData = Long.valueOf(data);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(bos);
            dos.writeLong(longData);
            dos.close();
            packet.setData(bos.toByteArray());
            socket.send(packet);
            System.out.println("send long " + longData);
        }
    }

    public static void main(String[] args) throws IOException {
        new TransClient().talk();
    }
}
