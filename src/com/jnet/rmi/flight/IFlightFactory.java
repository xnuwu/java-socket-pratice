package com.jnet.rmi.flight;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-12
 * @version 1.0.0
 */
public interface IFlightFactory extends Remote {

    public IFlight getFlight(String flightNumber) throws RemoteException;
}
