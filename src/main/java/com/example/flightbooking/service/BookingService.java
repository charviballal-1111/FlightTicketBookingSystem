package com.example.flightbooking.service;

import com.example.flightbooking.dto.BookingRequest;
import com.example.flightbooking.dto.BookingResponse;
import com.example.flightbooking.exception.FlightNotFoundException;
import com.example.flightbooking.exception.NotEnoughSeatsException;
import com.example.flightbooking.model.Booking;
import com.example.flightbooking.model.Flight;
import com.example.flightbooking.repository.InMemoryFlightRepository;
import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    private final InMemoryFlightRepository flightRepository;
    private final Clock clock;

    public BookingService(InMemoryFlightRepository flightRepository, Clock clock) {
        this.flightRepository = flightRepository;
        this.clock = clock;
    }

    public BookingResponse bookSeats(BookingRequest request) {
        Flight flight = flightRepository.findByFlightNumber(request.flightNumber())
                .orElseThrow(() -> new FlightNotFoundException(request.flightNumber()));

        Flight.ReservationResult reservation = flight.reserveSeats(request.seatCount());
        if (!reservation.accepted()) {
            throw new NotEnoughSeatsException(
                    flight.getFlightNumber(),
                    request.seatCount(),
                    reservation.remainingSeats()
            );
        }

        Booking booking = new Booking(
                UUID.randomUUID().toString(),
                flight.getFlightNumber(),
                request.passengerName(),
                request.passengerEmail(),
                request.seatCount(),
                Instant.now(clock)
        );

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
