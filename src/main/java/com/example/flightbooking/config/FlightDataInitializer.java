package com.example.flightbooking.config;

import com.example.flightbooking.model.Flight;
import com.example.flightbooking.repository.InMemoryFlightRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlightDataInitializer {

    @Bean
    CommandLineRunner initializeFlights(InMemoryFlightRepository flightRepository) {
        return args -> {
            flightRepository.save(new Flight("AI101", 4));
            flightRepository.save(new Flight("BA202", 8));
            flightRepository.save(new Flight("UA303", 12));
        };
    }
}
