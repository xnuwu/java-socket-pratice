package com.jnet.udp.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Date;

/**
 * @author Xunwu Yang 2021-01-09
 * @version 1.0.0
 */
public class MulticastSender {

    public static void main(String[] args) throws IOException {
        InetAddress group = InetAddress.getByName("230.0.0.0");
        int port = 4000;
        MulticastSocket multicastSocket = null;

        try{
            multicastSocket = new MulticastSocket(port);
            while (true) {
                String message = "i'm sending data at " + new Date();
                byte[] buffer = message.getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, group, port);
                multicastSocket.send(datagramPacket);
                System.out.println("发送数据包给" + group + ":" + port);
                Thread.sleep(1000);
            }
        }catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if(multicastSocket != null) {
                multicastSocket.leaveGroup(group);
                multicastSocket.close();
            }
        }
    }
}
