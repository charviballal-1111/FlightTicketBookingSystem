/******************************************************************************
Copyright (c) 2026
SOFTWARE     : Flight Ticket Booking System
FILENAME     : BookingRequest.java
DESCRIPTION  : Record to define booking request payload fields
Date         : 07/06/2026
Author       : @charviballal
********************************************************************************/
package com.example.flightbooking.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BookingRequest(
        @NotBlank(message = "flightNumber is required")
        String flightNumber,

        @NotBlank(message = "passengerName is required")
        String passengerName,

        @NotBlank(message = "passengerEmail is required")
        @Email(message = "passengerEmail must be a valid email address")
        String passengerEmail,

        @NotNull(message = "seatCount is required")
        @Min(value = 1, message = "seatCount must be at least 1")
        Integer seatCount
) {
    /**
     *
     * This compact constructor trims incoming string fields before validation
     * and booking processing.
     */
    public BookingRequest {
        flightNumber = trimToNullSafeValue(flightNumber);
        passengerName = trimToNullSafeValue(passengerName);
        passengerEmail = trimToNullSafeValue(passengerEmail);
    }

    /**
     *
     * This method trims a string value while preserving null inputs.
     *
     * @param value the value to trim
     * @return the trimmed value or null
     */
    private static String trimToNullSafeValue(String value) {
        return value == null ? null : value.trim();
    }
}
