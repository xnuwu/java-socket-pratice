package com.jnet.udp;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.text.DecimalFormat;

/**
 * @author Xunwu Yang 2020-12-31
 * @version 1.0.0
 */
public class DatagramTester {

    private static int port = 8000;
    private static final int MAX_LENGTH = 8096;
    private static DatagramSocket sendSocket;
    private static DatagramSocket receiveSocket;

    public static void main(String[] args) throws SocketException {
        sendSocket = new DatagramSocket();
        receiveSocket = new DatagramSocket(port);
        receiver.start();
        sender.start();
    }

    private static byte[] longToByte(long[] longData) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        for(int i = 0; i < longData.length; i++) {
            dos.writeLong(longData[i]);
        }

        dos.close();
        return baos.toByteArray();
    }

    private static long[] byteToLong(byte[] byteData) throws IOException {
        long[] result = new long[byteData.length / 8];
        ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
        DataInputStream dis = new DataInputStream(bais);
        for(int i = 0; i < byteData.length / 8; i++) {
            result[i] = dis.readLong();
        }
        return result;
    }

    private static Thread sender = new Thread() {
        @Override
        public void run() {
            long[] longArray = new long[MAX_LENGTH / 8];
            for(int i = 0; i < longArray.length; i++) {
                longArray[i] = i + 1;
            }
            try {
                send(longArray);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private static Thread receiver = new Thread() {
        @Override
        public void run() {
            try {
                byte[] receiveByte = receive();
                long[] longData = byteToLong(receiveByte);
                for(int i = 0; i < longData.length; i++) {
                    System.out.printf("%5d", longData[i]);

                    if((i + 1) % 30 == 0) {
                        System.out.println();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    public static void send(long[] longData) throws IOException {

        long beginTime = System.currentTimeMillis();
        int alreadySend = 0;
        int sendCount = 0;
        byte[] byteData = longToByte(longData);
        DatagramPacket packet = new DatagramPacket(byteData, 0, 512, InetAddress.getByName("localhost"), port);
        DecimalFormat decimalFormat = new DecimalFormat("#00.00%");
        while (alreadySend < byteData.length) {
            sendSocket.send(packet);
            alreadySend += packet.getLength();
            System.out.println("第" + (++sendCount) + "次发送" + packet.getLength() + "字节,已发送(" + decimalFormat.format((alreadySend * 1.0 / byteData.length)) + ")");
            int remain = byteData.length - alreadySend;
            int length = remain < 512 ? remain : 512;
            packet.setData(byteData, alreadySend, length);
        }
        DecimalFormat sizeFormat = new DecimalFormat("#####.00");
        System.out.println("数据发送完毕，累计发送" + sizeFormat.format((alreadySend * 1.0 / 1024)) + "kb,耗时" + (System.currentTimeMillis() - beginTime) + "ms");
        System.out.println();
    }

    public static byte[] receive() throws IOException {
        byte[] byteData = new byte[MAX_LENGTH];
        DatagramPacket packet = new DatagramPacket(byteData, byteData.length);

        int byteReceived = 0;
        int receiveCount = 0;
        long beginTime = System.currentTimeMillis();

        while (byteReceived < byteData.length && (System.currentTimeMillis() - beginTime) < 6000 * 5) {
            receiveSocket.receive(packet);
            System.out.println("第" + (++receiveCount) + "次接收到" + packet.getLength() + "字节数据");
            byteReceived += packet.getLength();
            packet.setData(byteData, byteReceived, MAX_LENGTH - byteReceived);
        }
        System.out.println("接收完毕,耗时" + (System.currentTimeMillis() - beginTime) + "ms");
        System.out.println();

        return byteData;
    }
}
