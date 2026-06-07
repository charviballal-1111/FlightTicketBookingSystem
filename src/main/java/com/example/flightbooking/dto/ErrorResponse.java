/******************************************************************************
Copyright (c) 2026
SOFTWARE     : Flight Ticket Booking System
FILENAME     : ErrorResponse.java
DESCRIPTION  : Record to define API error response fields
Date         : 07/06/2026
Author       : @charviballal
********************************************************************************/
package com.example.flightbooking.dto;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> details
) {
    /**
     *
     * This method creates an error response without additional detail entries.
     *
     * @param status the HTTP status code
     * @param error the HTTP reason phrase
     * @param message the error message
     * @param path the request path
     * @return the error response
     */
    public static ErrorResponse of(int status, String error, String message, String path) {
        return new ErrorResponse(Instant.now(), status, error, message, path, List.of());
    }

    /**
     *
     * This method creates an error response with additional detail entries.
     *
     * @param status the HTTP status code
     * @param error the HTTP reason phrase
     * @param message the error message
     * @param path the request path
     * @param details the detailed error messages
     * @return the error response
     */
    public static ErrorResponse withDetails(int status, String error, String message, String path, List<String> details) {
        return new ErrorResponse(Instant.now(), status, error, message, path, List.copyOf(details));
    }
}
