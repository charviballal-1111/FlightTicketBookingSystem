/******************************************************************************
Copyright (c) 2026
SOFTWARE     : Flight Ticket Booking System
FILENAME     : Flight.java
DESCRIPTION  : Class to define flight inventory and seat reservation behavior
Date         : 07/06/2026
Author       : @charviballal
********************************************************************************/
package com.example.flightbooking.model;

public class Flight {

    private final String flightNumber;
    private final int capacity;
    private int bookedSeats;

    /**
     *
     * This constructor creates a flight with a flight number and total seat
     * capacity.
     *
     * @param flightNumber the unique flight number
     * @param capacity the total number of seats available on the flight
     */
    public Flight(String flightNumber, int capacity) {
        if (flightNumber == null || flightNumber.isBlank()) {
            throw new IllegalArgumentException("flightNumber is required");
        }
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be at least 1");
        }
        this.flightNumber = flightNumber.trim();
        this.capacity = capacity;
    }

    /**
     *
     * This method returns the flight number assigned to the flight.
     *
     * @return the flight number
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     *
     * This method returns the configured capacity for the flight.
     *
     * @return the total seat capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     *
     * This method returns the number of seats already booked for the flight.
     *
     * @return the booked seat count
     */
    public synchronized int getBookedSeats() {
        return bookedSeats;
    }

    /**
     *
     * This method calculates the seats still available for booking.
     *
     * @return the remaining seat count
     */
    public synchronized int getRemainingSeats() {
        return capacity - bookedSeats;
    }

    /**
     *
     * This method attempts to reserve the requested number of seats without
     * allowing the booked count to exceed capacity.
     *
     * @param seatCount the number of seats requested
     * @return the reservation result with acceptance status and remaining seats
     */
    public synchronized ReservationResult reserveSeats(int seatCount) {
        if (seatCount < 1) {
            throw new IllegalArgumentException("seatCount must be at least 1");
        }
        int remainingSeats = capacity - bookedSeats;
        if (seatCount > remainingSeats) {
            return ReservationResult.rejected(remainingSeats);
        }
        bookedSeats += seatCount;
        return ReservationResult.accepted(capacity - bookedSeats);
    }

    public record ReservationResult(boolean accepted, int remainingSeats) {

        /**
         *
         * This method creates an accepted reservation result.
         *
         * @param remainingSeats the seats left after reservation
         * @return the accepted reservation result
         */
        static ReservationResult accepted(int remainingSeats) {
            return new ReservationResult(true, remainingSeats);
        }

        /**
         *
         * This method creates a rejected reservation result.
         *
         * @param remainingSeats the seats available when reservation failed
         * @return the rejected reservation result
         */
        static ReservationResult rejected(int remainingSeats) {
            return new ReservationResult(false, remainingSeats);
        }
    }
}
