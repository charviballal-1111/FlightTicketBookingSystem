/******************************************************************************
Copyright (c) 2026
SOFTWARE     : Flight Ticket Booking System
FILENAME     : DuplicatePassengerEmailException.java
DESCRIPTION  : Class to represent duplicate passenger email booking errors
Date         : 07/06/2026
Author       : @charviballal
********************************************************************************/
package com.example.flightbooking.exception;

public class DuplicatePassengerEmailException extends RuntimeException {

    /**
     *
     * This constructor creates an exception for a passenger email that already
     * has a booking.
     *
     * @param passengerEmail the duplicate passenger email
     */
    public DuplicatePassengerEmailException(String passengerEmail) {
        super("Passenger with email " + passengerEmail + " already has a booking");
    }
}
