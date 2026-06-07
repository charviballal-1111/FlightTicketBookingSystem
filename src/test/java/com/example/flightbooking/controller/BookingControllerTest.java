/******************************************************************************
Copyright (c) 2026
SOFTWARE     : Flight Ticket Booking System
FILENAME     : BookingControllerTest.java
DESCRIPTION  : Class to test booking controller HTTP behavior
Date         : 07/06/2026
Author       : @charviballal
********************************************************************************/
package com.example.flightbooking.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.flightbooking.dto.BookingRequest;
import com.example.flightbooking.exception.GlobalExceptionHandler;
import com.example.flightbooking.model.Flight;
import com.example.flightbooking.repository.InMemoryBookingRepository;
import com.example.flightbooking.repository.InMemoryFlightRepository;
import com.example.flightbooking.service.BookingService;
import java.time.Clock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import tools.jackson.databind.json.JsonMapper;

class BookingControllerTest {

    private final JsonMapper objectMapper = JsonMapper.builder().build();

    private MockMvc mockMvc;

    /**
     *
     * This method prepares the standalone MVC test environment before each
     * controller test.
     */
    @BeforeEach
    void setUp() {
        InMemoryFlightRepository repository = new InMemoryFlightRepository();
        repository.save(new Flight("AI101", 4));
        BookingService bookingService = new BookingService(
                repository,
                new InMemoryBookingRepository(),
                Clock.systemUTC()
        );
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders.standaloneSetup(new BookingController(bookingService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .setValidator(validator)
                .setMessageConverters(new JacksonJsonHttpMessageConverter(objectMapper))
                .build();
    }

    /**
     *
     * This method verifies that a valid booking request returns HTTP 201 and
     * the expected response body.
     *
     * @throws Exception when MVC request execution fails
     */
    @Test
    void createsBookingWithHttpCreated() throws Exception {
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BookingRequest(
                                " AI101 ",
                                " Asha Rao ",
                                " asha@example.com ",
                                2
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flightNumber").value("AI101"))
                .andExpect(jsonPath("$.passengerName").value("Asha Rao"))
                .andExpect(jsonPath("$.passengerEmail").value("asha@example.com"))
                .andExpect(jsonPath("$.seatsBooked").value(2))
                .andExpect(jsonPath("$.remainingSeats").value(2));
    }

    /**
     *
     * This method verifies that an unknown flight returns HTTP 404 with error
     * details.
     *
     * @throws Exception when MVC request execution fails
     */
    @Test
    void returnsNotFoundForUnknownFlight() throws Exception {
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BookingRequest(
                                "ZZ999",
                                "Asha Rao",
                                "asha@example.com",
                                1
                        ))))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Flight ZZ999 does not exist"))
                .andExpect(jsonPath("$.details[?(@ == 'flightNumber: Flight ZZ999 does not exist')]")
                        .exists());
    }

    /**
     *
     * This method verifies that unavailable seats return HTTP 409 with error
     * details.
     *
     * @throws Exception when MVC request execution fails
     */
    @Test
    void returnsConflictWhenSeatsAreUnavailable() throws Exception {
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BookingRequest(
                                "AI101",
                                "Asha Rao",
                                "asha@example.com",
                                5
                        ))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Flight AI101 has only 4 seats remaining; requested 5 seats"))
                .andExpect(jsonPath("$.details[?(@ == 'seatCount: Flight AI101 has only 4 seats remaining; requested 5 seats')]")
                        .exists());
    }

    /**
     *
     * This method verifies that duplicate passenger emails return HTTP 400 with
     * error details.
     *
     * @throws Exception when MVC request execution fails
     */
    @Test
    void returnsBadRequestWhenPassengerEmailAlreadyBooked() throws Exception {
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BookingRequest(
                                "AI101",
                                "Asha Rao",
                                "asha@example.com",
                                1
                        ))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BookingRequest(
                                "AI101",
                                "Asha Rao",
                                "asha@example.com",
                                1
                        ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Passenger with email asha@example.com already has a booking"))
                .andExpect(jsonPath("$.details[?(@ == 'passengerEmail: Passenger with email asha@example.com already has a booking')]")
                        .exists());
    }

    /**
     *
     * This method verifies that invalid request fields return HTTP 400 with
     * validation details.
     *
     * @throws Exception when MVC request execution fails
     */
    @Test
    void returnsBadRequestForInvalidRequest() throws Exception {
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BookingRequest(
                                "AI101",
                                "",
                                "not-an-email",
                                0
                        ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.details").isArray())
                .andExpect(jsonPath("$.details[?(@ == 'passengerName: passengerName is required')]").exists())
                .andExpect(jsonPath("$.details[?(@ == 'passengerEmail: passengerEmail must be a valid email address')]").exists())
                .andExpect(jsonPath("$.details[?(@ == 'seatCount: seatCount must be at least 1')]").exists());
    }

    /**
     *
     * This method verifies that malformed JSON returns HTTP 400 with request
     * body details.
     *
     * @throws Exception when MVC request execution fails
     */
    @Test
    void returnsBadRequestForMalformedJson() throws Exception {
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{not-json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request body is missing or malformed"))
                .andExpect(jsonPath("$.details[?(@ == 'requestBody: Request body is missing or malformed')]")
                        .exists());
    }
}
