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

    @Override
    public Object getContent(URLConnection connection, Class[] classes) throws IOException {
        InputStream inputStream = connection.getInputStream();
        for(int i = 0; i < classes.length; i++) {
            if(classes[i] == InputStream.class) {
                return inputStream;
            }else if(classes[i] == String.class) {
                return getContent(connection);
            }
        }

        return null;
    }
}
