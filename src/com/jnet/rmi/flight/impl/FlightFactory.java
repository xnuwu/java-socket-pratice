package com.jnet.rmi.flight.impl;

import com.jnet.rmi.flight.IFlight;
import com.jnet.rmi.flight.IFlightFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Xunwu Yang 2021-01-12
 * @version 1.0.0
 */
public class FlightFactory extends UnicastRemoteObject implements IFlightFactory {

    private Map<String, IFlight> flightMap = new HashMap<>();

    public FlightFactory() throws RemoteException {
    }

    @Override
    public IFlight getFlight(String flightNumber) throws RemoteException {
        IFlight flight = flightMap.get(flightNumber);

        if(flight != null) {
            return flight;
        }else {
            flight = new FlightImpl(flightNumber, null, null, null,null);
            flightMap.put(flightNumber, flight);
        }

        return flight;
    }
}
