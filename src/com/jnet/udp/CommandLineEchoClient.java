package com.jnet.udp;

import com.jnet.util.ByteBufferCodec;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * @author Xunwu Yang 2021-01-07
 * @version 1.0.0
 */
public class CommandLineEchoClient {

    private final DatagramChannel datagramChannel;
    private final Selector selector;
    private final ByteBuffer sendBuffer;
    private final ByteBuffer receiveBuffer;

    public CommandLineEchoClient() throws IOException {
        this(7000);
    }

    public CommandLineEchoClient(int port) throws IOException {
        datagramChannel = DatagramChannel.open();
        InetSocketAddress bindAddress = new InetSocketAddress(InetAddress.getLocalHost(), port);
        datagramChannel.configureBlocking(false);
        datagramChannel.socket().bind(bindAddress);
        datagramChannel.connect(new InetSocketAddress(InetAddress.getLocalHost(), 8000));
        sendBuffer = ByteBuffer.allocate(1024);
        receiveBuffer = ByteBuffer.allocate(1024);
        selector = Selector.open();
    }

    public static void main(String[] args) throws IOException {
        CommandLineEchoClient echoClient = new CommandLineEchoClient();
        Thread receiver = new Thread(() -> {
            try {
                echoClient.receiveFromUser();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        receiver.start();
        echoClient.talk();
    }

    private void receiveFromUser() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String message;
        while ((message = bufferedReader.readLine()) != null) {

            System.out.println("input:" + message);
            synchronized (sendBuffer) {
                sendBuffer.put((message + "\r\n").getBytes());
            }

            if(message.equalsIgnoreCase("bye")) {
                break;
            }
        }
    }

    private void talk() throws IOException {

        datagramChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);

        while (selector.select() > 0) {
            SelectionKey selectionKey = null;
            try{
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()) {
                    selectionKey = keyIterator.next();
                    keyIterator.remove();

                    if(selectionKey.isReadable()) {
                        receive(selectionKey);
                    }

                    if(selectionKey.isWritable()) {
                        send(selectionKey);
                    }
                }
            }catch (IOException e) {
                e.printStackTrace();

                selectionKey.cancel();
                selectionKey.channel().close();
            }
        }
    }

    private void receive(SelectionKey selectionKey) throws IOException {
        DatagramChannel datagramChannel = (DatagramChannel) selectionKey.channel();
        datagramChannel.read(receiveBuffer);
        receiveBuffer.flip();
        ByteBuffer readOnlyBuffer = receiveBuffer.asReadOnlyBuffer();
        String receiveMessage = ByteBufferCodec.decode(readOnlyBuffer);

        if(receiveMessage.contains("\r\n")) {

            String message = receiveMessage.substring(0, receiveMessage.indexOf("\n") + 1);
            System.out.println("receive:" + message);

            if(message.equalsIgnoreCase("echo:bye\r\n")) {
                selectionKey.cancel();
                datagramChannel.close();
                System.out.println("close connect!");
                selector.close();
                System.exit(0);
            }

            receiveBuffer.position(message.length());
            receiveBuffer.compact();
        }
    }

    private void send(SelectionKey selectionKey) throws IOException {
        DatagramChannel datagramChannel = (DatagramChannel) selectionKey.channel();
        synchronized (sendBuffer) {
            sendBuffer.flip();
            datagramChannel.write(sendBuffer);
            sendBuffer.compact();
        }
    }
}
