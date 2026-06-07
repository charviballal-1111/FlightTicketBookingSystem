package com.example.flightbooking.exception;

public class DuplicatePassengerEmailException extends RuntimeException {

    public DuplicatePassengerEmailException(String passengerEmail) {
        super("Passenger with email " + passengerEmail + " already has a booking");
    }
}
