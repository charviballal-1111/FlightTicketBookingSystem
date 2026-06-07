package com.example.flightbooking.exception;

public class NotEnoughSeatsException extends RuntimeException {

    public NotEnoughSeatsException(String flightNumber, int requestedSeats, int remainingSeats) {
        super(message(flightNumber, requestedSeats, remainingSeats));
    }

    private static String message(String flightNumber, int requestedSeats, int remainingSeats) {
        return "Flight " + flightNumber + " has only " + remainingSeats + " "
                + pluralize("seat", remainingSeats) + " remaining; requested "
                + requestedSeats + " " + pluralize("seat", requestedSeats);
    }

    private static String pluralize(String word, int count) {
        return count == 1 ? word : word + "s";
    }
}
