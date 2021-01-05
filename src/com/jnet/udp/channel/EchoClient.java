package com.jnet.udp.channel;

import com.jnet.util.ByteBufferCodec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @author Xunwu Yang 2021-01-05
 * @version 1.0.0
 */
public class EchoClient {
    public static void main(String[] args) throws IOException {
        DatagramChannel channel = DatagramChannel.open();
        InetSocketAddress serverAddress = new InetSocketAddress(InetAddress.getByName("localhost"), 8000);

        String inputMessage;
        ByteBuffer receiveBuffer = ByteBuffer.allocate(1024);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        while ((inputMessage = bufferedReader.readLine()) != null) {
            receiveBuffer.clear();
            channel.send(ByteBufferCodec.encode(inputMessage), serverAddress);
            System.out.println("send>" + inputMessage);
            channel.receive(receiveBuffer);
            receiveBuffer.flip();
            System.out.println("receive>" + ByteBufferCodec.decode(receiveBuffer));
        }
    }
}
