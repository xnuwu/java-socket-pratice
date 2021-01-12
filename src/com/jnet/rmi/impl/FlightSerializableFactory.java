package com.jnet.rmi.impl;

import com.jnet.rmi.IFlightSerializable;
import com.jnet.rmi.IFlightSerializableFactory;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Xunwu Yang 2021-01-12
 * @version 1.0.0
 */
public class FlightSerializableFactory extends UnicastRemoteObject implements IFlightSerializableFactory {

    private Map<String, IFlightSerializable> flightMap = new HashMap<>();

    public FlightSerializableFactory() throws RemoteException {
    }

    @Override
    public IFlightSerializable getFlight(String flightNumber) throws RemoteException {
        IFlightSerializable flight = flightMap.get(flightNumber);

        if(flight != null) {
            return flight;
        }else {
            flight = new FlightSerializableImpl(flightNumber, null, null, null, null);
            flightMap.put(flightNumber, flight);
        }

        return flight;
    }
}
