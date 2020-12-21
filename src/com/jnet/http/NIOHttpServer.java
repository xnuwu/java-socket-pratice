package com.jnet.http;

import com.jnet.http.nio.HttpServer;

import java.io.IOException;

/**
 * @author Xunwu Yang 2020-12-21
 * @version 1.0.0
 */
public class NIOHttpServer {
    public static void main(String[] args) throws IOException {
        new HttpServer().service();
    }
}
