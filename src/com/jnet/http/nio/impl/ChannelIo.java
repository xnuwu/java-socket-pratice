package com.jnet.http.nio.impl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author Xunwu Yang 2020-12-21
 * @version 1.0.0
 */
public class ChannelIo {

    private SocketChannel socketChannel;
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    private ByteBuffer readByteBuffer;

    public ChannelIo(SocketChannel socketChannel, boolean isBlock) throws IOException {
        this.socketChannel = socketChannel;
        this.socketChannel.configureBlocking(isBlock);
        readByteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE);
    }

    private void checkReadBufferSize(int allocateSize) {
        if(readByteBuffer.remaining() < allocateSize) {
            ByteBuffer newBuffer = ByteBuffer.allocate(readByteBuffer.capacity() * 2);
            readByteBuffer.flip();
            newBuffer.put(readByteBuffer);
            readByteBuffer = newBuffer;
        }
    }

    public int read() throws IOException {

        checkReadBufferSize(readByteBuffer.capacity() / 20);
        return socketChannel.read(readByteBuffer);
    }

    public ByteBuffer getReadByteBuffer() {
        return readByteBuffer;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public int write(ByteBuffer src) throws IOException {
        return socketChannel.write(src);
    }

    public long transferTo(FileChannel fileChannel, long pos, long len) throws IOException {
        return fileChannel.transferTo(pos, len, socketChannel);
    }

    public void close() throws IOException {
        socketChannel.close();
    }

}
