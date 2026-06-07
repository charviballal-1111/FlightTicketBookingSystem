/******************************************************************************
Copyright (c) 2026
SOFTWARE     : Flight Ticket Booking System
FILENAME     : InMemoryBookingRepository.java
DESCRIPTION  : Class to store and find bookings by passenger email in memory
Date         : 07/06/2026
Author       : @charviballal
********************************************************************************/
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

    /**
     *
     * This method finds a booking by the passenger email address.
     *
     * @param passengerEmail the passenger email to search for
     * @return the matching booking when available
     */
    public Optional<Booking> findByPassengerEmail(String passengerEmail) {
        return Optional.ofNullable(bookingsByPassengerEmail.get(emailKey(passengerEmail)));
    }

    /**
     *
     * This method saves a booking only when the passenger email has not already
     * been used.
     *
     * @param booking the booking to save
     * @return true when the booking was saved, otherwise false
     */
    public boolean saveIfPassengerEmailAvailable(Booking booking) {
        String emailKey = emailKey(booking.passengerEmail());
        return bookingsByPassengerEmail.putIfAbsent(emailKey, booking) == null;
    }

    /**
     *
     * This method deletes a booking only if it still matches the stored booking
     * for the passenger email.
     *
     * @param booking the booking to remove
     */
    public void deleteIfMatches(Booking booking) {
        bookingsByPassengerEmail.remove(emailKey(booking.passengerEmail()), booking);
    }

    /**
     *
     * This method normalizes a passenger email for duplicate booking checks.
     *
     * @param passengerEmail the passenger email to normalize
     * @return the normalized email key
     */
    private static String emailKey(String passengerEmail) {
        return passengerEmail == null ? "" : passengerEmail.trim().toLowerCase(Locale.ROOT);
    }
}
