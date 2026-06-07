/******************************************************************************
Copyright (c) 2026
SOFTWARE     : Flight Ticket Booking System
FILENAME     : BookingController.java
DESCRIPTION  : Class to expose booking REST endpoints
Date         : 07/06/2026
Author       : @charviballal
********************************************************************************/
package com.example.flightbooking.controller;

import com.example.flightbooking.dto.BookingRequest;
import com.example.flightbooking.dto.BookingResponse;
import com.example.flightbooking.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    /**
     *
     * This constructor creates the booking controller with its booking service
     * dependency.
     *
     * @param bookingService the service used to create bookings
     */
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     *
     * This method handles booking creation requests and returns the confirmed
     * booking response.
     *
     * @param request the validated booking request payload
     * @return the confirmed booking response
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(@Valid @RequestBody BookingRequest request) {
        return bookingService.bookSeats(request);
    }
}
