package com.jnet.udp.channel.multi;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Xunwu Yang 2021-01-05
 * @version 1.0.0
 */
public class ServerChannel {

    private static DatagramChannel datagramChannel;
    private static DatagramSocket datagramSocket;

    private static List<SocketAddress> socketAddressList = new LinkedList<SocketAddress>();

    public ServerChannel() throws IOException {
        datagramChannel = DatagramChannel.open();
        datagramSocket = datagramChannel.socket();
        datagramSocket.bind(new InetSocketAddress(8080));
    }

    public static void main(String[] args) throws IOException {
        new ServerChannel().service();
    }

    public void service() {
        while (true) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.clear();
            System.out.println("缓冲区剩余字节为:" + buffer.remaining());

        }
    }
}
