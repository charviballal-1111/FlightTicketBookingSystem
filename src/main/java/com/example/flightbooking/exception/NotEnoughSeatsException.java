package com.example.flightbooking.exception;

public class NotEnoughSeatsException extends RuntimeException {

    public NotEnoughSeatsException(String flightNumber, int requestedSeats, int remainingSeats) {
        super("Flight " + flightNumber + " has only " + remainingSeats
                + " seats remaining; requested " + requestedSeats);
    }
}
