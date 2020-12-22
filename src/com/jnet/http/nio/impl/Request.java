package com.jnet.http.nio.impl;

import com.jnet.util.ByteBufferCodec;

import java.nio.ByteBuffer;

/**
 * @author Xunwu Yang 2020-12-21
 * @version 1.0.0
 */
public class Request {

    /**
     * only receive header
     * @param byteBuffer
     * @return
     */
    public static boolean isComplete(ByteBuffer byteBuffer) {
        String decodeData = ByteBufferCodec.decode(byteBuffer);
        return decodeData.contains("\r\n\r\n");
    }
}
