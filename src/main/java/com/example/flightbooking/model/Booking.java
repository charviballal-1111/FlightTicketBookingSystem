package com.example.flightbooking.model;

import java.time.Instant;

public record Booking(
        String bookingReference,
        String flightNumber,
        String passengerName,
        String passengerEmail,
        int seatCount,
        Instant confirmedAt
) {
}
