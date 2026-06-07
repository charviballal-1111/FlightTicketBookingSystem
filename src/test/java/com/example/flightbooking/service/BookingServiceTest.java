package com.example.flightbooking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.flightbooking.dto.BookingRequest;
import com.example.flightbooking.dto.BookingResponse;
import com.example.flightbooking.exception.FlightNotFoundException;
import com.example.flightbooking.exception.NotEnoughSeatsException;
import com.example.flightbooking.model.Flight;
import com.example.flightbooking.repository.InMemoryFlightRepository;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

class BookingServiceTest {

    private static final Clock FIXED_CLOCK = Clock.fixed(
            Instant.parse("2026-06-07T08:00:00Z"),
            ZoneOffset.UTC
    );

    @Test
    void booksSeatsSuccessfully() {
        InMemoryFlightRepository repository = new InMemoryFlightRepository();
        repository.save(new Flight("AI101", 5));
        BookingService service = new BookingService(repository, FIXED_CLOCK);

        BookingResponse response = service.bookSeats(new BookingRequest(
                "AI101",
                "Asha Rao",
                "asha@example.com",
                2
        ));

        assertThat(response.bookingReference()).isNotBlank();
        assertThat(response.flightNumber()).isEqualTo("AI101");
        assertThat(response.passengerName()).isEqualTo("Asha Rao");
        assertThat(response.passengerEmail()).isEqualTo("asha@example.com");
        assertThat(response.seatsBooked()).isEqualTo(2);
        assertThat(response.remainingSeats()).isEqualTo(3);
        assertThat(response.confirmedAt()).isEqualTo(FIXED_CLOCK.instant());
        assertThat(repository.findByFlightNumber("AI101").orElseThrow().getBookedSeats()).isEqualTo(2);
    }

    @Test
    void rejectsBookingForUnknownFlight() {
        BookingService service = new BookingService(new InMemoryFlightRepository(), FIXED_CLOCK);

        assertThatThrownBy(() -> service.bookSeats(new BookingRequest(
                "ZZ999",
                "Asha Rao",
                "asha@example.com",
                1
        )))
                .isInstanceOf(FlightNotFoundException.class)
                .hasMessage("Flight ZZ999 does not exist");
    }

    @Test
    void rejectsOverbooking() {
        InMemoryFlightRepository repository = new InMemoryFlightRepository();
        repository.save(new Flight("AI101", 2));
        BookingService service = new BookingService(repository, FIXED_CLOCK);

        assertThatThrownBy(() -> service.bookSeats(new BookingRequest(
                "AI101",
                "Asha Rao",
                "asha@example.com",
                3
        )))
                .isInstanceOf(NotEnoughSeatsException.class)
                .hasMessage("Flight AI101 has only 2 seats remaining; requested 3");

        assertThat(repository.findByFlightNumber("AI101").orElseThrow().getBookedSeats()).isZero();
    }

    @Test
    void concurrentBookingsCannotExceedCapacity() throws Exception {
        int capacity = 20;
        int attempts = 100;
        InMemoryFlightRepository repository = new InMemoryFlightRepository();
        repository.save(new Flight("AI101", capacity));
        BookingService service = new BookingService(repository, FIXED_CLOCK);
        ExecutorService executor = Executors.newFixedThreadPool(attempts);
        CountDownLatch ready = new CountDownLatch(attempts);
        CountDownLatch start = new CountDownLatch(1);
        List<Callable<Boolean>> tasks = new ArrayList<>();

        for (int i = 0; i < attempts; i++) {
            tasks.add(() -> {
                ready.countDown();
                start.await();
                try {
                    service.bookSeats(new BookingRequest(
                            "AI101",
                            "Passenger",
                            "passenger@example.com",
                            1
                    ));
                    return true;
                } catch (NotEnoughSeatsException exception) {
                    return false;
                }
            });
        }

        List<Future<Boolean>> futures = tasks.stream()
                .map(executor::submit)
                .toList();
        assertThat(ready.await(5, TimeUnit.SECONDS)).isTrue();
        start.countDown();

        int successfulBookings = 0;
        for (Future<Boolean> future : futures) {
            if (future.get(5, TimeUnit.SECONDS)) {
                successfulBookings++;
            }
        }
        executor.shutdown();
        assertThat(executor.awaitTermination(5, TimeUnit.SECONDS)).isTrue();

        Flight flight = repository.findByFlightNumber("AI101").orElseThrow();
        assertThat(successfulBookings).isEqualTo(capacity);
        assertThat(flight.getBookedSeats()).isEqualTo(capacity);
        assertThat(flight.getRemainingSeats()).isZero();
    }
}
