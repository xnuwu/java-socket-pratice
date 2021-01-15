package com.jnet.rmi.stock.impl;

import com.jnet.rmi.stock.StockQuote;
import com.jnet.rmi.stock.StockQuoteRegistry;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Random;

/**
 * @author Xunwu Yang 2021-01-15
 * @version 1.0.0
 */
public class StockQuoteRegistryImpl extends UnicastRemoteObject implements StockQuoteRegistry, Runnable {

    private HashSet<StockQuote> stockQuoteSet = new HashSet<>();

    public StockQuoteRegistryImpl() throws RemoteException {
    }

    @Override
    public void registerClient(StockQuote client) throws RemoteException {
        if(stockQuoteSet.contains(client)) {
            return;
        }

        stockQuoteSet.add(client);
        System.out.println("add new client " + client.hashCode());
    }

    @Override
    public void unregisterClient(StockQuote client) throws RemoteException {
        System.err.println("remove illegal client " + client.hashCode());
        stockQuoteSet.remove(client);
    }

    @Override
    public void run() {

        String[] quotes = { "APPLE", "NIO", "PDD", "ZOOM", "MS", "GOOGLE" };
        Random random = new Random(1000);
        while (true) {
            try {
                if(!stockQuoteSet.isEmpty()) {
                    for(StockQuote client: stockQuoteSet) {
                        try{
                            client.timeLine();
                            for(String quote: quotes) {
                                client.quote(quote, random.nextDouble());
                            }
                        }catch (RemoteException e) {
                            unregisterClient(client);
                        }
                    }
                }

                Thread.sleep(1000);
            } catch (InterruptedException | RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
