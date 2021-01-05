package com.jnet.udp.channel;

import com.jnet.util.ByteBufferCodec;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @author Xunwu Yang 2021-01-05
 * @version 1.0.0
 */
public class SendChannel {

    public static void main(String[] args) throws IOException, InterruptedException {

        DatagramChannel datagramChannel = DatagramChannel.open();
        DatagramSocket datagramSocket = datagramChannel.socket();
        SocketAddress localAddress = new InetSocketAddress(7000);
        SocketAddress remoteAddress = new InetSocketAddress((InetAddress.getByName("localhost")), 8000);
        datagramSocket.bind(localAddress);

        while (true) {
            ByteBuffer buffer = ByteBufferCodec.encode("now time is " + System.currentTimeMillis());
            System.out.println("缓冲区剩余字节" + buffer.remaining());
            ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
            int n = datagramChannel.send(buffer, remoteAddress);
            System.out.println("发送字节数" + n + ", " + ByteBufferCodec.decode(readOnlyBuffer));
            Thread.sleep(5000);
        }
    }
}
