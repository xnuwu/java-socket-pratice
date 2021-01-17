package com.jnet.rmi.serializable.impl;

import com.jnet.rmi.serializable.IFlightSerializable;

import java.rmi.RemoteException;

/**
 * @author Xunwu Yang 2021-01-12
 * @version 1.0.0
 */
public class FlightSerializableImpl implements IFlightSerializable {

    private String flightNumber;
    private String origin;
    private String destination;
    private String skdDeparture;
    private String skdArrival;

    public FlightSerializableImpl(String flightNumber, String origin, String destination, String skdDeparture, String skdArrival) throws RemoteException {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.skdDeparture = skdDeparture;
        this.skdArrival = skdArrival;
    }

    public String getFlightNumber() throws RemoteException {
        return flightNumber;
    }

    
    public String getOrigin() throws RemoteException {
        return origin;
    }

    
    public String getDestination() throws RemoteException {
        return destination;
    }

    
    public String getSkdDeparture() throws RemoteException {
        return skdDeparture;
    }

    
    public String getSkdArrival() throws RemoteException {
        return skdArrival;
    }

    
    public void setFlightNumber(String flightNumber) throws RemoteException {
        this.flightNumber = flightNumber;
    }

    
    public void setOrigin(String origin) throws RemoteException {
        this.origin = origin;
    }

    
    public void setDestination(String destination) throws RemoteException {
        this.destination = destination;
    }

    
    public void setSkdDeparture(String skdDeparture) throws RemoteException {
        this.skdDeparture = skdDeparture;
    }

    
    public void setSkdArrival(String skdArrival) throws RemoteException {
        this.skdArrival = skdArrival;
    }

    
    public String toString() {
        return "FlightSerializableImpl{" +
                "flightNumber='" + flightNumber + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", skdDeparture='" + skdDeparture + '\'' +
                ", skdArrival='" + skdArrival + '\'' +
                '}';
    }
}
