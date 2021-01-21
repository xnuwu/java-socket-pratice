package com.jnet.ssl;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Xunwu Yang 2021-01-21
 * @version 1.0.0
 */
public class HTTPSClient {

    private SSLSocketFactory sslSocketFactory;
    private SSLSocket sslSocket;

    private String host = "zoom.us";
    private int port = 443;

    public void createSSLSocket() throws IOException {
        sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port);
        String[] suites = sslSocket.getSupportedCipherSuites();
        sslSocket.setEnabledCipherSuites(suites);

        sslSocket.addHandshakeCompletedListener(new HandshakeCompletedListener() {
            @Override
            public void handshakeCompleted(HandshakeCompletedEvent handshakeCompletedEvent) {
                System.out.println("握手结束");
                System.out.println("加密套件为" + handshakeCompletedEvent.getCipherSuite());
                System.out.println("会话为" + handshakeCompletedEvent.getSession());
                System.out.println("通信对方为" + handshakeCompletedEvent.getSession().getPeerHost());
            }
        });
    }

    public void communicate() throws Exception {

        StringBuffer buffer = new StringBuffer("GET https://" + host + " HTTP/1.1\r\n");
        buffer.append("Host:" + host + "\r\n");
        buffer.append("Accept:*/*\r\n");
        buffer.append("\r\n");

        OutputStream outputStream = sslSocket.getOutputStream();
        outputStream.write(buffer.toString().getBytes());

        InputStream inputStream = sslSocket.getInputStream();
        ByteArrayOutputStream dataBuffer = new ByteArrayOutputStream();

        byte[] buff = new byte[1024];
        int len;
        while ((len = inputStream.read(buff)) != -1) {
            dataBuffer.write(buff, 0, len);
            System.out.println("读取:" + len + "字节，已写入缓存");
        }

        System.out.println("读取数据结束" + dataBuffer.size());
        System.out.println(dataBuffer.toString("gb2312"));

        sslSocket.close();
    }

    public static void main(String[] args) throws Exception {
        HTTPSClient httpsClient = new HTTPSClient();
        httpsClient.createSSLSocket();
        httpsClient.communicate();
    }
}
