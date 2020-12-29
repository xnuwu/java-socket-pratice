package com.jnet.connection.echo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ContentHandler;
import java.net.URLConnection;

/**
 * @author Xunwu Yang 2020-12-29
 * @version 1.0.0
 */
public class EchoContentHandler extends ContentHandler {

    @Override
    public Object getContent(URLConnection connection) throws IOException {
        InputStream inputStream = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        return bufferedReader.readLine();
    }
}
