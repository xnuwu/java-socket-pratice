package com.jnet.rmi.flight.impl;

import com.jnet.rmi.flight.IFlight;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Xunwu Yang 2021-01-12
 * @version 1.0.0
 */
public class FlightImpl extends UnicastRemoteObject implements IFlight {

    private String flightNumber;
    private String origin;
    private String destination;
    private String skdDeparture;
    private String skdArrival;

    public FlightImpl(String flightNumber, String origin, String destination, String skdDeparture, String skdArrival) throws RemoteException {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.skdDeparture = skdDeparture;
        this.skdArrival = skdArrival;
    }

    @Override
    public String getFlightNumber() throws RemoteException {
        return flightNumber;
    }

    @Override
    public String getOrigin() throws RemoteException {
        return origin;
    }

    @Override
    public String getDestination() throws RemoteException {
        return destination;
    }

    @Override
    public String getSkdDeparture() throws RemoteException {
        return skdDeparture;
    }

    @Override
    public String getSkdArrival() throws RemoteException {
        return skdArrival;
    }

    @Override
    public void setFlightNumber(String flightNumber) throws RemoteException {
        this.flightNumber = flightNumber;
    }

    @Override
    public void setOrigin(String origin) throws RemoteException {
        this.origin = origin;
    }

    @Override
    public void setDestination(String destination) throws RemoteException {
        this.destination = destination;
    }

    @Override
    public void setSkdDeparture(String skdDeparture) throws RemoteException {
        this.skdDeparture = skdDeparture;
    }

    @Override
    public void setSkdArrival(String skdArrival) throws RemoteException {
        this.skdArrival = skdArrival;
    }

    @Override
    public String toString() {
        return "FlightImpl{" +
                "flightNumber='" + flightNumber + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", skdDeparture='" + skdDeparture + '\'' +
                ", skdArrival='" + skdArrival + '\'' +
                '}';
    }
}
