package com.jnet.rmi.impl;

import com.jnet.rmi.IHelloService;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * @author: yangxunwu
 * @date: 2021/1/11 16:49
 */
public class HelloServiceImpl extends UnicastRemoteObject implements IHelloService {

    private String name;

    public HelloServiceImpl(String name) throws RemoteException {
        this.name = name;
    }

    @Override
    public String echo(String message) throws IOException {
        System.out.println("invoke echo");
        return "echo:" + message;
    }

    @Override
    public Date time() throws Exception {
        return new Date();
    }
}
