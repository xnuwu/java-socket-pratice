package com.jnet.rmi.stock;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-14
 * @version 1.0.0
 */
public interface StockQuoteRegistry extends Remote {

    public void registerClient(StockQuote client) throws RemoteException;

    public void unregisterClient(StockQuote client) throws RemoteException;
}
