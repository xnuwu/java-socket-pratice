package com.jnet.rmi.concurrent.impl;

import com.jnet.rmi.concurrent.Stack;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Xunwu Yang 2021-01-17
 * @version 1.0.0
 */
public class StackImpl extends UnicastRemoteObject implements Stack {

    private String name;
    private int point;
    private String[] buffer;

    public StackImpl(String name) throws RemoteException {
        this.name = name;
        this.point = -1;
        buffer = new String[16];
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public synchronized int getPoint() throws RemoteException {
        return point;
    }

    @Override
    public synchronized void push(String good) throws RemoteException {
        try{
            while (point == buffer.length - 1) {
                wait();
            }

            point++;
            buffer[point] = good;
            System.out.println("push " + good + " at point " + point);

            notifyAll();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized String pop() throws RemoteException {
        String good = null;
        try{
            while (point == -1) {
                wait();
            }

            good = buffer[point];
            point--;
            System.out.println("pop " + good + " from point " + (point + 1));

            notifyAll();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        return good;
    }
}
