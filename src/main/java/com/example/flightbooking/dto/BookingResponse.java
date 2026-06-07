/******************************************************************************
Copyright (c) 2026
SOFTWARE     : Flight Ticket Booking System
FILENAME     : BookingResponse.java
DESCRIPTION  : Record to define successful booking response fields
Date         : 07/06/2026
Author       : @charviballal
********************************************************************************/
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
