package com.jnet.rmi.stock;

import com.jnet.rmi.stock.impl.StockQuoteImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-15
 * @version 1.0.0
 */
public class StockClient {

    public static void main(String[] args) throws NamingException, RemoteException {
        Context context = new InitialContext();
        StockQuoteRegistry stockQuoteRegistry = (StockQuoteRegistry) context.lookup("rmi://localhost/StockQuoteService");

        StockQuote stockQuote = new StockQuoteImpl();
        stockQuoteRegistry.registerClient(stockQuote);
    }
}
