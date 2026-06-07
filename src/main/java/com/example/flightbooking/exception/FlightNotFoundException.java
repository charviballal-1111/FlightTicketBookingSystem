/******************************************************************************
Copyright (c) 2026
SOFTWARE     : Flight Ticket Booking System
FILENAME     : FlightNotFoundException.java
DESCRIPTION  : Class to represent a missing flight error
Date         : 07/06/2026
Author       : @charviballal
********************************************************************************/
package com.example.flightbooking.exception;

public class FlightNotFoundException extends RuntimeException {

    /**
     *
     * This constructor creates an exception for a requested flight number that
     * does not exist.
     *
     * @param flightNumber the missing flight number
     */
    public FlightNotFoundException(String flightNumber) {
        super("Flight " + flightNumber + " does not exist");
    }
}
