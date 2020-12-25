package com.jnet.http.nio.impl;

import com.jnet.http.nio.IHandler;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 处理连接操作，注册监听OP_READ操作，通过RequestHandler统一处理请求的连接
 * @author Xunwu Yang 2020-12-21
 * @version 1.0.0
 */
public class AcceptHandler implements IHandler {

    @Override
    public void handle(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();

        //no block mode will return null
        if(socketChannel != null) {
            System.out.println("receive connected from " + socketChannel.socket().getInetAddress() + ":" + socketChannel.socket().getPort());
            ChannelIo channelIo = new ChannelIo(socketChannel, false);
            RequestHandler requestHandler = new RequestHandler(channelIo);
            socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ, requestHandler);
        }
    }
}