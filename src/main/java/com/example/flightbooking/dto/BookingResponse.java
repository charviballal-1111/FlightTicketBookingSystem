package com.example.flightbooking.dto;

import java.time.Instant;

public record BookingResponse(
        String bookingReference,
        String flightNumber,
        String passengerName,
        String passengerEmail,
        int seatsBooked,
        int remainingSeats,
        Instant confirmedAt
) {
}
