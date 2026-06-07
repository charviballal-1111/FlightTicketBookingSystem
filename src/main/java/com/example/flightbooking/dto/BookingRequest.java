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
    public BookingRequest {
        flightNumber = trimToNullSafeValue(flightNumber);
        passengerName = trimToNullSafeValue(passengerName);
        passengerEmail = trimToNullSafeValue(passengerEmail);
    }

    private static String trimToNullSafeValue(String value) {
        return value == null ? null : value.trim();
    }
}
