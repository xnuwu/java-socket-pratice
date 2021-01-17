package com.jnet.rmi.gc;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-17
 * @version 1.0.0
 */
public class SimpleClient {
    public static void main(String[] args) throws NamingException, RemoteException, InterruptedException {
        Context context = new InitialContext();
        HelloService helloService = (HelloService) context.lookup("rmi://localhost/HelloService");
        helloService.access();
        Thread.sleep(500);
        helloService.bye();
    }
}
