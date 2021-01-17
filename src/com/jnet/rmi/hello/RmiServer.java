package com.jnet.rmi.hello;

import com.jnet.rmi.hello.impl.HelloServiceImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-11
 * @version 1.0.0
 */
public class RmiServer {

    public static void main(String[] args) throws RemoteException, NamingException {

        IRemoteHelloService helloService1 = new HelloServiceImpl("helloService-1");
        IRemoteHelloService helloService2 = new HelloServiceImpl("helloService-2");

        Context namingContext = new InitialContext();
        namingContext.rebind("rmi:HelloService1", helloService1);
        namingContext.rebind("rmi:HelloService2", helloService2);
        System.out.println("服务注册了两个HelloService服务");
    }
}
