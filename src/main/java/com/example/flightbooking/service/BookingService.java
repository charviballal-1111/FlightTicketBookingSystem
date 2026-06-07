/******************************************************************************
Copyright (c) 2026
SOFTWARE     : Flight Ticket Booking System
FILENAME     : BookingService.java
DESCRIPTION  : Class to coordinate booking validation and seat reservation
Date         : 07/06/2026
Author       : @charviballal
********************************************************************************/
package com.example.flightbooking.service;

import com.example.flightbooking.dto.BookingRequest;
import com.example.flightbooking.dto.BookingResponse;
import com.example.flightbooking.exception.DuplicatePassengerEmailException;
import com.example.flightbooking.exception.FlightNotFoundException;
import com.example.flightbooking.exception.NotEnoughSeatsException;
import com.example.flightbooking.model.Booking;
import com.example.flightbooking.model.Flight;
import com.example.flightbooking.repository.InMemoryBookingRepository;
import com.example.flightbooking.repository.InMemoryFlightRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final InMemoryFlightRepository flightRepository;
    private final InMemoryBookingRepository bookingRepository;
    private final Clock clock;

    /**
     *
     * This constructor creates the booking service with flight storage, booking
     * storage, and clock dependencies.
     *
     * @param flightRepository the repository used to find flights
     * @param bookingRepository the repository used to track passenger bookings
     * @param clock the clock used to timestamp confirmations
     */
    public BookingService(
            InMemoryFlightRepository flightRepository,
            InMemoryBookingRepository bookingRepository,
            Clock clock
    ) {
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
        this.clock = clock;
    }

    /**
     *
     * This method books seats for a passenger after validating flight
     * availability and duplicate passenger email rules.
     *
     * @param request the booking request submitted by the client
     * @return the confirmed booking response
     */
    public BookingResponse bookSeats(BookingRequest request) {
        Flight flight = flightRepository.findByFlightNumber(request.flightNumber())
                .orElseThrow(() -> new FlightNotFoundException(request.flightNumber()));

        Booking booking = new Booking(
                UUID.randomUUID().toString(),
                flight.getFlightNumber(),
                request.passengerName(),
                request.passengerEmail(),
                request.seatCount(),
                Instant.now(clock)
        );

        if (!bookingRepository.saveIfPassengerEmailAvailable(booking)) {
            throw new DuplicatePassengerEmailException(request.passengerEmail());
        }

        Flight.ReservationResult reservation;
        try {
            reservation = flight.reserveSeats(request.seatCount());
        } catch (RuntimeException exception) {
            bookingRepository.deleteIfMatches(booking);
            throw exception;
        }

        if (!reservation.accepted()) {
            bookingRepository.deleteIfMatches(booking);
            throw new NotEnoughSeatsException(
                    flight.getFlightNumber(),
                    request.seatCount(),
                    reservation.remainingSeats()
            );
        }

        return new BookingResponse(
                booking.bookingReference(),
                booking.flightNumber(),
                booking.passengerName(),
                booking.passengerEmail(),
                booking.seatCount(),
                reservation.remainingSeats(),
                booking.confirmedAt()
        );
    }
}
