package com.jnet.socket;

import java.io.IOException;
import java.net.*;

/**
 * a connect test program for specific host:port from cli param
 */
public class ConnectTester {

    private String host;
    private int port;
    private String hostPort;

    public ConnectTester(String host, int port) {
        this.host = host;
        this.port = port;
        this.hostPort = host + ":" + port;
    }

    public static void main(String[] args) {
        String host = "127.0.0.1";
        int port = 20;
        if(args.length >= 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        }

        System.out.println(new ConnectTester(host, port).connect());
        System.out.println(new ConnectTester(host, port).connect());
        System.out.println(new ConnectTester(host, port).connect());
    }

    private String connect() {

        SocketAddress socketAddress = new InetSocketAddress(host, port);
        Socket socket = null;
        int timeout = 0;

        String ret;
        try{
            long beginTime = System.currentTimeMillis();

            socket = new Socket();
            socket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 20));
            socket.connect(socketAddress, timeout);

            long endTime = System.currentTimeMillis();

            ret = "connected to " + hostPort + " success, cost time " + (endTime - beginTime) + "ms";
        }catch (BindException e) {
            ret = hostPort + " has been already bind, " + e.getMessage();
        }catch (UnknownHostException e) {
            ret = host + " can't be resolved, " + e.getMessage();
        }catch (ConnectException e) {
            ret = hostPort + " can't be connected, " + e.getMessage();
        }catch (SocketTimeoutException e) {
            ret = "connect to " + hostPort + " timeout in " + timeout + "ms, " + e.getMessage();
        }catch (IOException e) {
            ret = hostPort + " " + e.getMessage();
        }finally {
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }
}
