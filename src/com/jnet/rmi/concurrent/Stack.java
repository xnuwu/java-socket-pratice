package com.jnet.rmi.concurrent;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-17
 * @version 1.0.0
 */
public interface Stack extends Remote {

    public String getName() throws RemoteException;

    public int getPoint() throws RemoteException;

    public void push(String good) throws RemoteException;

    public String pop() throws RemoteException;
}
