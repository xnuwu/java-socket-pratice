package com.jnet.nio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class NIOEchoClient {

    private Charset charset;
    private SocketChannel socketChannel;
    private Selector selector;
    private ByteBuffer receiveBuffer;
    private ByteBuffer sendBuffer;

    public NIOEchoClient() throws IOException {

        charset = Charset.forName("UTF-8");
        receiveBuffer = ByteBuffer.allocate(1024);
        sendBuffer = ByteBuffer.allocate(1024);
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 2020));
        socketChannel.configureBlocking(false);
    }

    public void receiveFromUserInput() {

        System.out.println("please input message to send:");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String message;

        try{
            while ((message = reader.readLine()) != null) {
                synchronized (sendBuffer) {
                    message = message + "\n";
                    if(sendBuffer.remaining() > message.length()) {
                        sendBuffer.put(message.getBytes());
                    }else{
                        System.out.println("send buffer no enough space!");
                    }
                }

                if(message.equals("bye\n")) {
                    break;
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void talk() throws IOException {
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        while (selector.select() > 0) {
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = null;
                try{
                    selectionKey = keyIterator.next();
                    keyIterator.remove();

                    if(selectionKey.isWritable()) {
                        send(selectionKey);
                    }

                    if(selectionKey.isReadable()) {
                        receive(selectionKey);
                    }
                }catch (IOException e) {
                    try{
                        selectionKey.cancel();
                        selectionKey.channel().close();
                    }catch (Exception se) {
                        se.printStackTrace();
                    }
                }
            }
        }

    }

    public void send(SelectionKey selectionKey) throws IOException {

        SocketChannel channel = (SocketChannel) selectionKey.channel();
        synchronized (sendBuffer) {
            sendBuffer.flip();
            channel.write(sendBuffer);
            sendBuffer.compact();
        }
    }

    public void receive(SelectionKey selectionKey) throws IOException {

        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        int readNum = socketChannel.read(receiveBuffer);

        if(readNum == 0) {
            return;
        }

        receiveBuffer.flip();
        CharBuffer charBuffer = charset.decode(receiveBuffer);
        String data = charBuffer.toString();
        String outputData = null;

        if(data.contains("\n")) {
            outputData = data.substring(0, data.indexOf("\n") + 1);
            System.out.println(outputData);

            if(outputData.trim().equals("echo: bye")) {
                selectionKey.cancel();
                socketChannel.close();
                System.out.println("closed connection!");
                selector.close();
                System.exit(0);
            }
        }

        if(outputData != null) {
            ByteBuffer temp = charset.encode(outputData);
            receiveBuffer.position(temp.limit());
            receiveBuffer.compact();
        }
    }

    public static void main(String[] args) throws IOException {

        NIOEchoClient nioEchoClient = new NIOEchoClient();
        new Thread(new Runnable() {
            @Override
            public void run() {
                nioEchoClient.receiveFromUserInput();
            }
        }).start();

        nioEchoClient.talk();
    }
}
