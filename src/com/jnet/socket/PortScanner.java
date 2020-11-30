package com.jnet.socket;

import java.io.IOException;
import java.net.*;

/**
 * a simple port scanner
 */
public class PortScanner {

    private String host;

    public PortScanner(String host) {
        this.host = host;
    }

    public static void main(String[] args) throws UnknownHostException {
        new PortScanner("127.0.0.1").scan();
    }

    public void scan() throws UnknownHostException {
        Socket socket = null;
        InetAddress address = InetAddress.getByName(host);
        for (int port = 1; port < 10240; port++) {
            try {
                socket = new Socket();
                SocketAddress socketAddress = new InetSocketAddress(address, port);

                //set timeout 10ms
                socket.connect(socketAddress, 10);
                System.out.println(host + ":" + port + " is opened");
            } catch (IOException e) {
                //System.out.println(host + ":" + port + " is closed");
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
