package com.jnet.rmi.gc;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-17
 * @version 1.0.0
 */
public interface HelloService extends Remote {
    public boolean isAccessed() throws RemoteException;
    public void access() throws RemoteException;
    public void bye() throws RemoteException;
}
