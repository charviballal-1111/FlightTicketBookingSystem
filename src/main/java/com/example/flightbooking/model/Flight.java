package com.example.flightbooking.model;

public class Flight {

    private final String flightNumber;
    private final int capacity;
    private int bookedSeats;

    public Flight(String flightNumber, int capacity) {
        if (flightNumber == null || flightNumber.isBlank()) {
            throw new IllegalArgumentException("flightNumber is required");
        }
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be at least 1");
        }
        this.flightNumber = flightNumber;
        this.capacity = capacity;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public synchronized int getBookedSeats() {
        return bookedSeats;
    }

    public synchronized int getRemainingSeats() {
        return capacity - bookedSeats;
    }

    public synchronized boolean reserveSeats(int seatCount) {
        if (seatCount < 1) {
            throw new IllegalArgumentException("seatCount must be at least 1");
        }
        if (seatCount > getRemainingSeats()) {
            return false;
        }
        bookedSeats += seatCount;
        return true;
    }
}
