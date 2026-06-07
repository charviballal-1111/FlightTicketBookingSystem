/******************************************************************************
Copyright (c) 2026
SOFTWARE     : Flight Ticket Booking System
FILENAME     : InMemoryFlightRepository.java
DESCRIPTION  : Class to store and retrieve flights in memory
Date         : 07/06/2026
Author       : @charviballal
********************************************************************************/
package com.example.flightbooking.repository;

import com.example.flightbooking.model.Flight;
import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryFlightRepository {

    private final ConcurrentMap<String, Flight> flights = new ConcurrentHashMap<>();

    /**
     *
     * This method saves a flight in the in-memory repository.
     *
     * @param flight the flight to save
     * @return the saved flight
     */
    public Flight save(Flight flight) {
        flights.put(flight.getFlightNumber(), flight);
        return flight;
    }

    /**
     *
     * This method finds a flight by its flight number.
     *
     * @param flightNumber the flight number to search for
     * @return the matching flight when available
     */
    public Optional<Flight> findByFlightNumber(String flightNumber) {
        if (flightNumber == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(flights.get(flightNumber.trim()));
    }

    /**
     *
     * This method returns all flights currently stored in memory.
     *
     * @return the collection of stored flights
     */
    public Collection<Flight> findAll() {
        return flights.values();
    }
}
