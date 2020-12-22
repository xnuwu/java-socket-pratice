package com.jnet.http.nio.impl;

import com.jnet.http.nio.Sendable;

import java.io.IOException;

/**
 * @author: yangxunwu
 * @date: 2020/12/22 12:08
 */
public class Response implements Sendable {
    @Override
    public void prepare() throws IOException {

    }

    @Override
    public boolean send(ChannelIo channelIo) throws IOException {
        return false;
    }

    @Override
    public void release() throws IOException {

    }
}
