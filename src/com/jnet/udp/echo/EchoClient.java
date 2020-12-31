package com.jnet.udp.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author Xunwu Yang 2020-12-29
 * @version 1.0.0
 */
public class EchoClient {

    private String remoteHost = "localhost";
    private int remotePort = 8000;
    private DatagramSocket socket;

    public EchoClient() throws IOException {
        socket = new DatagramSocket();
    }

    public void talk() throws IOException {
        try{
            InetAddress remoteIP = InetAddress.getByName(remoteHost);
            BufferedReader localReader = new BufferedReader(new InputStreamReader(System.in));
            String message;

            while ((message = localReader.readLine()) != null) {
                byte[] outputData = message.getBytes();
                DatagramPacket packet = new DatagramPacket(outputData, outputData.length, remoteIP, remotePort);
                socket.send(packet);

                DatagramPacket receivedPacket = new DatagramPacket(new byte[1024], 1024);
                socket.receive(receivedPacket);
                System.out.println(new String(receivedPacket.getData(), 0, receivedPacket.getLength()));

                if(message.equals("bye")) {
                    break;
                }
            }

        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            socket.close();
        }
    }

    public static void main(String[] args) throws IOException {
        new EchoClient().talk();
    }
}
