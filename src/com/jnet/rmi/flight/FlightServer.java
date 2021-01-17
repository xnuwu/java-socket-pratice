package com.jnet.rmi.flight;

import com.jnet.rmi.flight.impl.FlightFactory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-12
 * @version 1.0.0
 */
public class FlightServer {

    public static void main(String[] args) throws NamingException, RemoteException {

        IFlightFactory flightFactory = new FlightFactory();

        Context context = new InitialContext();
        context.rebind("rmi:FlightFactory", flightFactory);

        System.out.println("注册了一个rmi服务");
    }
}
