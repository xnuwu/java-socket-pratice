package com.jnet.http.nio.impl;

import com.jnet.http.nio.IHandler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author Xunwu Yang 2020-12-21
 * @version 1.0.0
 */
public class AcceptHandler implements IHandler {

    @Override
    public void handle(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();

    }
}