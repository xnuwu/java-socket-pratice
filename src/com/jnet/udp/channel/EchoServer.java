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
public class EchoServer {

    private int port = 8000;
    private DatagramChannel channel;
    private final int MAX_SIZE = 1024;

    public EchoServer() throws IOException {
        channel = DatagramChannel.open();
        DatagramSocket socket = channel.socket();
        socket.bind(new InetSocketAddress(port));
        System.out.println("server listen at " + port);
    }

    public String echo(String message) {
        return "echo:" + message;
    }

    public void service() {
        ByteBuffer buffer = ByteBuffer.allocate(MAX_SIZE);
        while (true) {
            buffer.clear();
            try {
                final SocketAddress clientAddress = channel.receive(buffer);
                buffer.flip();
                String message = ByteBufferCodec.decode(buffer);
                System.out.println("client:>" + message);

                String responseMessage = echo(message);
                channel.send(ByteBufferCodec.encode(responseMessage), clientAddress);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new EchoServer().service();
    }
}
