package com.jnet.reflect.remoteCall.impl;

import com.jnet.reflect.remoteCall.IHelloService;

import java.util.Date;

/**
 * @author Xunwu Yang 2021-01-10
 * @version 1.0.0
 */
public class HelloServiceImpl implements IHelloService {

    @Override
    public String echo(String message) {
        return "echo:" + message;
    }

    @Override
    public Date time() {
        return new Date();
    }
}
