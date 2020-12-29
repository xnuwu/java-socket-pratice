package com.jnet.connection.echo;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

/**
 * @author: yangxunwu
 * @date: 2020/12/29 18:55
 */
public class EchoURLStreamHandlerFactory implements URLStreamHandlerFactory {

    private static final String ECHO_PROTOCOL_PREFIX = "echo";

    @Override
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if(protocol.equals(ECHO_PROTOCOL_PREFIX)) {
            return new EchoURLStreamHandler();
        }else {
            return null;
        }
    }
}
