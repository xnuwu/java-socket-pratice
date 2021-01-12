package com.jnet.rmi;

import com.jnet.rmi.impl.FlightSerializableFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-12
 * @version 1.0.0
 */
public class FlightSerializableServer {

    public static void main(String[] args) throws NamingException, RemoteException {

        IFlightSerializableFactory flightFactory = new FlightSerializableFactory();

        Context context = new InitialContext();
        context.rebind("rmi:FlightSerializableFactory", flightFactory);

        System.out.println("注册了一个rmi serializable服务");
    }
}
