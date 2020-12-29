package com.jnet.connection.echo;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * @author: yangxunwu
 * @date: 2020/12/29 18:53
 */
public class EchoURLStreamHandler extends URLStreamHandler {

    @Override
    public int getDefaultPort() {
        return 8000;
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new EchoURLConnection(u);
    }
}
