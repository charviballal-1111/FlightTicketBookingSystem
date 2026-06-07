/******************************************************************************
Copyright (c) 2026
SOFTWARE     : Flight Ticket Booking System
FILENAME     : FlightDataInitializer.java
DESCRIPTION  : Class to configure startup data and application clock
Date         : 07/06/2026
Author       : @charviballal
********************************************************************************/
package com.example.flightbooking.config;

import com.example.flightbooking.model.Flight;
import com.example.flightbooking.repository.InMemoryFlightRepository;
import java.time.Clock;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlightDataInitializer {

    /**
     *
     * This method creates the clock bean used by booking services.
     *
     * @return the UTC system clock
     */
    @Bean
    Clock bookingClock() {
        return Clock.systemUTC();
    }

    /**
     *
     * This method seeds the in-memory flight repository when the application
     * starts.
     *
     * @param flightRepository the repository that stores seeded flights
     * @return the command-line runner that initializes flight data
     */
    @Bean
    CommandLineRunner initializeFlights(InMemoryFlightRepository flightRepository) {
        return args -> {
            flightRepository.save(new Flight("AI101", 4));
            flightRepository.save(new Flight("BA202", 8));
            flightRepository.save(new Flight("UA303", 12));
        };
    }
}
