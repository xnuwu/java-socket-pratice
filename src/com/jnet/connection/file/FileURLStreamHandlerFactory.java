package com.jnet.connection.file;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

/**
 * @author Xunwu Yang 2020-12-29
 * @version 1.0.0
 */
public class FileURLStreamHandlerFactory implements URLStreamHandlerFactory {
    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        return new FileURLStreamHandler();
    }
}
