package com.jnet.udp.channel;

import com.jnet.util.ByteBufferCodec;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @author Xunwu Yang 2021-01-05
 * @version 1.0.0
 */
public class ReceiveChannel {

    public static void main(String[] args) throws IOException, InterruptedException {
        final int ENOUGH_SIZE = 1024;
        final int SMALL_SIZE = 4;

        boolean isBlock = false;
        int size = ENOUGH_SIZE;

        if(args.length > 0) {
            int opt = Integer.parseInt(args[0]);
            switch (opt) {
                case 1: isBlock = true; size = ENOUGH_SIZE; break;
                case 2: isBlock = true; size = SMALL_SIZE; break;
                case 3: isBlock = false; size = ENOUGH_SIZE; break;
                case 4: isBlock = false; size = SMALL_SIZE; break;
            }
        }

        DatagramChannel datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(isBlock);
        DatagramSocket datagramSocket = datagramChannel.socket();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8000);
        datagramSocket.bind(inetSocketAddress);

        ByteBuffer buffer = ByteBuffer.allocate(size);

        while (true) {
            buffer.clear();
            System.out.println("开始接受数据报");
            SocketAddress remoteAddress = datagramChannel.receive(buffer);
            if(remoteAddress == null) {
                System.out.println("没有接收到数据!");
            }else{
                buffer.flip();
                System.out.println("接收到的数据包大小: " + buffer.remaining() + " " + (ByteBufferCodec.decode(buffer)));
            }
            Thread.sleep(5000);
        }
    }
}
