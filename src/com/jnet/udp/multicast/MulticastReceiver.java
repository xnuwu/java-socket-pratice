package com.jnet.udp.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * @author Xunwu Yang 2021-01-09
 * @version 1.0.0
 */
public class MulticastReceiver {

    public static void main(String[] args) throws UnknownHostException {
        InetAddress group = InetAddress.getByName("230.0.0.0");
        int port = 4000;
        MulticastSocket multicastSocket = null;

        try{
            multicastSocket = new MulticastSocket(port);
            multicastSocket.joinGroup(group);

            byte[] buffer = new byte[8192];
            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(datagramPacket);
                String s = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(multicastSocket == null) {
                try {
                    multicastSocket.leaveGroup(group);
                    multicastSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
