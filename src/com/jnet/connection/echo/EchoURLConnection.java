package com.jnet.connection.echo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author: yangxunwu
 * @date: 2020/12/29 14:57
 */
public class EchoURLConnection extends URLConnection {

    private Socket connection = null;

    public EchoURLConnection(URL url) {
        super(url);
    }

    @Override
    public synchronized InputStream getInputStream() throws IOException {
        if(!connected) {
            connect();
        }
        return connection.getInputStream();
    }

    @Override
    public synchronized OutputStream getOutputStream() throws IOException {
        if(!connected) {
            connect();
        }

        return connection.getOutputStream();
    }

    @Override
    public String getContentType() {
        return "text/plain";
    }

    @Override
    public void connect() throws IOException {
        if(!connected) {
            int port = url.getPort();
            if(port < 0 || port >= 65535) {
                throw new IllegalArgumentException("invalid port " + port);
            }
            this.connection = new Socket(url.getHost(), port);
            this.connected = true;
        }
    }

    public synchronized void disconnect() throws IOException {
        if(connected) {
            this.connection.close();
            this.connected = false;
        }
    }
}
