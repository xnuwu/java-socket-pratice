package com.jnet.rmi.dynamicLoad;

import com.jnet.rmi.dynamicLoad.impl.HelloServiceImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-17
 * @version 1.0.0
 */
public class SimpleServer {
    public static void main(String[] args) throws RemoteException, NamingException {
        IHelloService helloService = new HelloServiceImpl("hello");
        Context namingContext = new InitialContext();
        namingContext.rebind("rmi:HelloService", helloService);
    }
}