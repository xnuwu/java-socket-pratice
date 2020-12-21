package com.jnet.http.nio;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * @author Xunwu Yang 2020-12-21
 * @version 1.0.0
 */
public interface IHandler {

    /**
     * handle specific SelectionKey
     * @param selectionKey
     */
    void handle(SelectionKey selectionKey) throws IOException;
}
