package com.jnet.udp;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author Xunwu Yang 2020-12-29
 * @version 1.0.0
 */
public class EchoServer {

    private int port = 8000;
    private DatagramSocket socket;

    public EchoServer() throws IOException {
        socket = new DatagramSocket(port);
        System.out.println("服务器启动成功~");
    }

    public String echo(String filename) throws IOException {

        System.out.println("try to read file " + filename);
        StringBuffer stringBuffer = new StringBuffer();
        File file = new File(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

        String line;
        while ((line = reader.readLine()) != null) {
            stringBuffer.append(line);
        }
        System.out.println("read file " + filename + ", size " + stringBuffer.length());
        return stringBuffer.toString();
    }

    public void service() {
        while (true) {
            try{
                System.out.println("==============================================");
                DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println(packet.getAddress() + ":" + packet.getPort() + "> length:" + message.length());

                packet.setData(echo(message).getBytes());
                socket.send(packet);

            }catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new EchoServer().service();
    }
}
