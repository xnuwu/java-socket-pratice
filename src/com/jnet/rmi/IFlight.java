package com.jnet.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-12
 * @version 1.0.0
 */
public interface IFlight extends Remote {

    public String getFlightNumber() throws RemoteException;

    public String getOrigin() throws RemoteException;

    public String getDestination() throws RemoteException;

    public String getSkdDeparture()  throws RemoteException;

    public String getSkdArrival() throws RemoteException;

    public void setFlightNumber(String flightNumber) throws RemoteException;

    public void setOrigin(String origin) throws RemoteException;

    public void setDestination(String destination) throws RemoteException;

    public void setSkdDeparture(String skdDeparture)  throws RemoteException;

    public void setSkdArrival(String skdArrival) throws RemoteException;
}
