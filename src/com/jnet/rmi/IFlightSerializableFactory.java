package com.jnet.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-12
 * @version 1.0.0
 */
public interface IFlightSerializableFactory extends Remote {

    public IFlightSerializable getFlight(String flightNumber) throws RemoteException;
}
