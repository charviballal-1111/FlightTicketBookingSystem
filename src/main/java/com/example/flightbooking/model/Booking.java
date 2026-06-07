/******************************************************************************
Copyright (c) 2026
SOFTWARE     : Flight Ticket Booking System
FILENAME     : Booking.java
DESCRIPTION  : Record to define confirmed booking details
Date         : 07/06/2026
Author       : @charviballal
********************************************************************************/
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
