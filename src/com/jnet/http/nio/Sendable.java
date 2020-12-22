package com.jnet.http.nio;

import com.jnet.http.nio.impl.ChannelIo;

import java.io.IOException;

/**
 * @author Xunwu Yang 2020-12-21
 * @version 1.0.0
 */
public interface Sendable {

    void prepare() throws IOException;

    boolean send(ChannelIo channelIo) throws IOException;

    void release() throws IOException;
}
