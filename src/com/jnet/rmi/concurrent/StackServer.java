package com.jnet.rmi.concurrent;

import com.jnet.rmi.concurrent.impl.StackImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-17
 * @version 1.0.0
 */
public class StackServer {
    public static void main(String[] args) throws RemoteException, NamingException {
        Stack stack = new StackImpl("my-stack");
        Context context = new InitialContext();

        context.rebind("rmi:myStack", stack);
    }
}
