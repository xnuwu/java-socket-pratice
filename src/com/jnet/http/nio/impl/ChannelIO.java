package com.jnet.http.nio.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author Xunwu Yang 2020-12-21
 * @version 1.0.0
 */
public class ChannelIO {

    private SocketChannel socketChannel;

    private static final int defaultBufferSize = 4096;

    private ByteBuffer readByteBuffer;

    private boolean readFinishFlag = false;


    public ChannelIO(SocketChannel socketChannel, boolean isBlock) {
        this.socketChannel = socketChannel;
        readByteBuffer = ByteBuffer.allocate(defaultBufferSize);
    }


    private void checkReadBufferSize(int allocateSize) {
        if(readByteBuffer.remaining() < allocateSize) {
            ByteBuffer newBuffer = ByteBuffer.allocate(readByteBuffer.capacity() * 2);
            readByteBuffer.flip();
            newBuffer.put(readByteBuffer);
            readByteBuffer = newBuffer;
        }
    }

    public boolean read() throws IOException {

        checkReadBufferSize(readByteBuffer.capacity() / 20);
        if(readFinishFlag) {
            return false;
        }
        if(socketChannel.read(readByteBuffer) < 0 || Request.isComplete(readByteBuffer)) {
            readFinishFlag = true;
            return false;
        }
        return true;
    }

    public ByteBuffer getReadByteBuffer() {
        return readByteBuffer;
    }
}
