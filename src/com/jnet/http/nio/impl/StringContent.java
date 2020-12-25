package com.jnet.http.nio.impl;

import com.jnet.http.nio.Content;
import com.jnet.util.ByteBufferCodec;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Xunwu Yang 2020-12-22
 * @version 1.0.0
 */
public class StringContent implements Content {
    private String content;
    private long length;

    public StringContent(String message) {
        this.content = message;
        this.length = message.length();
    }

    @Override
    public String type() {
        return "text/plain;charset=utf-8";
    }

    @Override
    public long length() {
        return length;
    }

    @Override
    public void prepare() throws IOException {

    }

    @Override
    public boolean send(ChannelIo channelIo) throws IOException {
        ByteBuffer responseByteBuffer = ByteBufferCodec.encode(content);
        responseByteBuffer.flip();

        while (responseByteBuffer.hasRemaining()) {
            channelIo.write(responseByteBuffer);
        }
        return false;
    }

    @Override
    public void release() throws IOException {

    }
}
