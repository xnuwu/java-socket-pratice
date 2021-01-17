package com.jnet.rmi.serializable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-12
 * @version 1.0.0
 */
public class FlightSerializableClient {
    public static void main(String[] args) throws NamingException, RemoteException {

        Context context = new InitialContext();
        IFlightSerializableFactory flightFactory = (IFlightSerializableFactory) context.lookup("rmi://localhost/FlightSerializableFactory");
        IFlightSerializable flight1 = flightFactory.getFlight("F-1234");
        flight1.setDestination("zehua");
        flight1.setOrigin("hefei");
        flight1.setSkdArrival("mexico");
        flight1.setSkdDeparture("anhui");
        System.out.println("flight1:" + flight1);
        System.out.println(flight1.getClass() + " " + flight1.getFlightNumber() + " " + flight1.getDestination() + " " + flight1.getOrigin());

        IFlightSerializable flight2 = flightFactory.getFlight("F-1234");
        System.out.println("flight2:" + flight2);
        System.out.println("flight1 == flight2:" + (flight1 == flight2));
        System.out.println("flight1 equal flight2:" + (flight1.equals(flight2)));
        System.out.println(flight2.getClass() + " " +flight2.getFlightNumber() + " " + flight2.getDestination() + " " + flight2.getOrigin());
    }
}
