package com.jnet.reflect.remoteCall.proxy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Xunwu Yang 2021-01-10
 * @version 1.0.0
 */
public class Connector {

    private String host;
    private int port;
    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    public Connector(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        connect();
    }

    public void connect() throws IOException {
        System.out.println("try connect to " + host + ":" + port);
        socket =  new Socket(InetAddress.getLocalHost(), port);
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
        System.out.println("connect success!");
    }

    public void send(Object object) throws IOException {
        oos.writeObject(object);
    }

    public Object receive() throws IOException, ClassNotFoundException {
        return ois.readObject();
    }

    public void close() throws IOException {
        if(socket != null) {
            ois.close();
            oos.close();
            socket.close();
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
