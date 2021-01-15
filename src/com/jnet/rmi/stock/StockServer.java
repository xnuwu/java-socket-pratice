package com.jnet.rmi.stock;

import com.jnet.rmi.stock.impl.StockQuoteRegistryImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-15
 * @version 1.0.0
 */
public class StockServer {

    public static void main(String[] args) throws NamingException, RemoteException {

        Context context = new InitialContext();
        StockQuoteRegistryImpl stockQuoteRegistry = new StockQuoteRegistryImpl();
        context.rebind("rmi:StockQuoteService", stockQuoteRegistry);

        new Thread(stockQuoteRegistry).start();
    }
}
