package com.jnet.http.nio;

import com.jnet.http.nio.impl.AcceptHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

/**
 * @author Xunwu Yang 2020-12-21
 * @version 1.0.0
 */
public class HttpServer {

    private static int defaultPort = 20;
    private int port;

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public HttpServer() throws IOException {
        this(defaultPort);
    }

    public HttpServer(int port) throws IOException {
        this.port = port;

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(port));
        serverSocketChannel.socket().setReuseAddress(true);
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, new AcceptHandler());

        System.out.println("HttpServer Listen at port " + port);
    }

    public void service() throws IOException {
        while (true) {
            int selectNum = selector.select();
            if(selectNum == 0) {
                return;
            }

            Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
            while (selectionKeyIterator.hasNext()) {
                SelectionKey selectionKey = null;
                try{
                    selectionKey = selectionKeyIterator.next();
                    selectionKeyIterator.remove();

                    final IHandler handler = (IHandler) selectionKey.attachment();
                    handler.handle(selectionKey);
                }catch (IOException e) {
                    e.printStackTrace();
                    selectionKey.cancel();
                    selectionKey.channel().close();
                }
            }
        }
    }
}
