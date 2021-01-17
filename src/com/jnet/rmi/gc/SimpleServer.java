package com.jnet.rmi.gc;

import com.jnet.rmi.gc.impl.HelloServiceImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-17
 * @version 1.0.0
 */
public class SimpleServer {

    public static void main(String[] args) throws RemoteException, NamingException, InterruptedException {
        System.setProperty("java.rmi.dgc.leaseValue", "3000");
        HelloService service = new HelloServiceImpl();
        Context context = new InitialContext();
        context.rebind("rmi:HelloService", service);
        System.out.println("注册服务HelloService");

        while (!service.isAccessed()) {
            Thread.sleep(100);
        }
        context.unbind("rmi:HelloService");
        System.out.println("注销服务HelloService");
    }
}
