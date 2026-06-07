/******************************************************************************
Copyright (c) 2026
SOFTWARE     : Flight Ticket Booking System
FILENAME     : GlobalExceptionHandler.java
DESCRIPTION  : Class to convert application exceptions into API error responses
Date         : 07/06/2026
Author       : @charviballal
********************************************************************************/
package com.example.flightbooking.exception;

import com.example.flightbooking.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     *
     * This method handles missing flight exceptions and returns a not found API
     * error response.
     *
     * @param exception the missing flight exception
     * @param request the servlet request that caused the exception
     * @return the not found error response
     */
    @ExceptionHandler(FlightNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFlightNotFound(
            FlightNotFoundException exception,
            HttpServletRequest request
    ) {
        ErrorResponse response = ErrorResponse.withDetails(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI(),
                List.of("flightNumber: " + exception.getMessage())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     *
     * This method handles insufficient seat exceptions and returns a conflict
     * API error response.
     *
     * @param exception the insufficient seats exception
     * @param request the servlet request that caused the exception
     * @return the conflict error response
     */
    @ExceptionHandler(NotEnoughSeatsException.class)
    public ResponseEntity<ErrorResponse> handleNotEnoughSeats(
            NotEnoughSeatsException exception,
            HttpServletRequest request
    ) {
        ErrorResponse response = ErrorResponse.withDetails(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI(),
                List.of("seatCount: " + exception.getMessage())
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     *
     * This method handles duplicate passenger email exceptions and returns a bad
     * request API error response.
     *
     * @param exception the duplicate passenger email exception
     * @param request the servlet request that caused the exception
     * @return the bad request error response
     */
    @ExceptionHandler(DuplicatePassengerEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatePassengerEmail(
            DuplicatePassengerEmailException exception,
            HttpServletRequest request
    ) {
        ErrorResponse response = ErrorResponse.withDetails(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                exception.getMessage(),
                request.getRequestURI(),
                List.of("passengerEmail: " + exception.getMessage())
        );
        return ResponseEntity.badRequest().body(response);
    }

    /**
     *
     * This method handles bean validation failures and returns a bad request API
     * error response with field-specific details.
     *
     * @param exception the validation exception
     * @param request the servlet request that caused the exception
     * @return the bad request error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        List<String> details = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatFieldError)
                .toList();

        ErrorResponse response = ErrorResponse.withDetails(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Request validation failed",
                request.getRequestURI(),
                details
        );
        return ResponseEntity.badRequest().body(response);
    }

    /**
     *
     * This method handles missing or malformed request bodies and returns a bad
     * request API error response.
     *
     * @param exception the unreadable request exception
     * @param request the servlet request that caused the exception
     * @return the bad request error response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadableRequest(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) {
        ErrorResponse response = ErrorResponse.withDetails(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Request body is missing or malformed",
                request.getRequestURI(),
                List.of("requestBody: Request body is missing or malformed")
        );
        return ResponseEntity.badRequest().body(response);
    }

    /**
     *
     * This method formats a validation field error for the response details
     * list.
     *
     * @param error the validation field error
     * @return the formatted field error message
     */
    private String formatFieldError(FieldError error) {
        return error.getField() + ": " + error.getDefaultMessage();
    }
}
