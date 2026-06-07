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

    public Flight save(Flight flight) {
        flights.put(flight.getFlightNumber(), flight);
        return flight;
    }

    public Optional<Flight> findByFlightNumber(String flightNumber) {
        return Optional.ofNullable(flights.get(flightNumber));
    }

    public Collection<Flight> findAll() {
        return flights.values();
    }
}
