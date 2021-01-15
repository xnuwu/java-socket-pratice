package com.jnet.rmi.stock.impl;

import com.jnet.rmi.stock.StockQuote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;

/**
 * @author Xunwu Yang 2021-01-15
 * @version 1.0.0
 */
public class StockQuoteImpl extends UnicastRemoteObject implements StockQuote {

    public StockQuoteImpl() throws RemoteException {
    }

    @Override
    public void timeLine() throws RemoteException {
        System.out.println("------------------------ " + new Date() + "------------------------ ");
    }

    @Override
    public void quote(String stockSymbol, double price) throws RemoteException {
        System.out.printf("%10s%5.2f\r\n", stockSymbol, price);
    }
}
