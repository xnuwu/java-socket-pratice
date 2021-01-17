package com.jnet.rmi.gc.impl;

import com.jnet.rmi.gc.HelloService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;

/**
 * @author Xunwu Yang 2021-01-17
 * @version 1.0.0
 */
public class HelloServiceImpl extends UnicastRemoteObject implements HelloService, Unreferenced {

    private boolean access;

    public HelloServiceImpl() throws RemoteException {
        this.access = false;
    }

    @Override
    public boolean isAccessed() throws RemoteException {
        return access;
    }

    @Override
    public void access() throws RemoteException {
        System.out.println("HelloServiceImpl被使用");
        access = true;
    }

    @Override
    public void bye() throws RemoteException {
        System.out.println("HelloServiceImpl准备结束使用");
    }

    @Override
    public void unreferenced() {
        System.out.println("~HelloServiceImpl准备解应用");
    }
}
