/******************************************************************************
Copyright (c) 2026
SOFTWARE     : Flight Ticket Booking System
FILENAME     : FlightBookingApplication.java
DESCRIPTION  : Class to bootstrap the flight booking Spring Boot application
Date         : 07/06/2026
Author       : @charviballal
********************************************************************************/
package com.example.flightbooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FlightBookingApplication {

    /**
     *
     * This method starts the flight booking application by delegating to
     * Spring Boot's application runner.
     *
     * @param args command-line arguments supplied to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(FlightBookingApplication.class, args);
    }
}
