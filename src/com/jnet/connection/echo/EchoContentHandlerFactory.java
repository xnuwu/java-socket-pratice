package com.jnet.connection.echo;

import java.net.ContentHandler;
import java.net.ContentHandlerFactory;

/**
 * @author Xunwu Yang 2020-12-29
 * @version 1.0.0
 */
public class EchoContentHandlerFactory implements ContentHandlerFactory {

    private static final String ECHO_MIME_TYPE = "text/plain";

    @Override
    public ContentHandler createContentHandler(String mimetype) {
        if(mimetype.equals(ECHO_MIME_TYPE)) {
            return new EchoContentHandler();
        }else{
            return null;
        }
    }
}
