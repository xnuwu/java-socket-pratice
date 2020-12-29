package com.jnet.connection.file;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * @author Xunwu Yang 2020-12-29
 * @version 1.0.0
 */
public class FileURLStreamHandler extends URLStreamHandler {
    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return new FileURLConnection(u);
    }
}
