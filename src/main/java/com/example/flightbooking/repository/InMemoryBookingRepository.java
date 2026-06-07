package com.example.flightbooking.repository;

import com.example.flightbooking.model.Booking;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryBookingRepository {

    private final ConcurrentMap<String, Booking> bookingsByPassengerEmail = new ConcurrentHashMap<>();

    public Optional<Booking> findByPassengerEmail(String passengerEmail) {
        return Optional.ofNullable(bookingsByPassengerEmail.get(emailKey(passengerEmail)));
    }

    public boolean saveIfPassengerEmailAvailable(Booking booking) {
        String emailKey = emailKey(booking.passengerEmail());
        return bookingsByPassengerEmail.putIfAbsent(emailKey, booking) == null;
    }

    public void deleteIfMatches(Booking booking) {
        bookingsByPassengerEmail.remove(emailKey(booking.passengerEmail()), booking);
    }

    private static String emailKey(String passengerEmail) {
        return passengerEmail == null ? "" : passengerEmail.trim().toLowerCase(Locale.ROOT);
    }
}
