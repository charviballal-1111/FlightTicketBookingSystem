/******************************************************************************
Copyright (c) 2026
SOFTWARE     : Flight Ticket Booking System
FILENAME     : NotEnoughSeatsException.java
DESCRIPTION  : Class to represent insufficient seat availability errors
Date         : 07/06/2026
Author       : @charviballal
********************************************************************************/
package com.example.flightbooking.exception;

public class NotEnoughSeatsException extends RuntimeException {

    /**
     *
     * This constructor creates an exception when a booking request asks for more
     * seats than a flight has remaining.
     *
     * @param flightNumber the flight number being booked
     * @param requestedSeats the number of seats requested
     * @param remainingSeats the number of seats available
     */
    public NotEnoughSeatsException(String flightNumber, int requestedSeats, int remainingSeats) {
        super(message(flightNumber, requestedSeats, remainingSeats));
    }

    /**
     *
     * This method builds the insufficient seats error message.
     *
     * @param flightNumber the flight number being booked
     * @param requestedSeats the number of seats requested
     * @param remainingSeats the number of seats available
     * @return the formatted error message
     */
    private static String message(String flightNumber, int requestedSeats, int remainingSeats) {
        return "Flight " + flightNumber + " has only " + remainingSeats + " "
                + pluralize("seat", remainingSeats) + " remaining; requested "
                + requestedSeats + " " + pluralize("seat", requestedSeats);
    }

    /**
     *
     * This method pluralizes a word based on the provided count.
     *
     * @param word the word to pluralize
     * @param count the count used to determine plurality
     * @return the singular or plural word
     */
    private static String pluralize(String word, int count) {
        return count == 1 ? word : word + "s";
    }
}
