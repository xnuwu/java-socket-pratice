package com.jnet.rmi.stock;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-14
 * @version 1.0.0
 */
public interface StockQuote extends Remote {
    public void timeLine() throws RemoteException;
    public void quote(String stockSymbol, double price) throws RemoteException;
}
