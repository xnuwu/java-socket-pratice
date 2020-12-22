package com.jnet.http.nio;

/**
 * @author Xunwu Yang 2020-12-21
 * @version 1.0.0
 */
public interface Content extends Sendable {
    
    String type();

    long length();
}
