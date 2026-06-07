package com.example.flightbooking.model;

public class Flight {

    private final String flightNumber;
    private final int capacity;
    private int bookedSeats;

    public Flight(String flightNumber, int capacity) {
        if (flightNumber == null || flightNumber.isBlank()) {
            throw new IllegalArgumentException("flightNumber is required");
        }
        if (capacity < 1) {
            throw new IllegalArgumentException("capacity must be at least 1");
        }
        this.flightNumber = flightNumber.trim();
        this.capacity = capacity;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public synchronized int getBookedSeats() {
        return bookedSeats;
    }

    public synchronized int getRemainingSeats() {
        return capacity - bookedSeats;
    }

    public synchronized ReservationResult reserveSeats(int seatCount) {
        if (seatCount < 1) {
            throw new IllegalArgumentException("seatCount must be at least 1");
        }
        int remainingSeats = capacity - bookedSeats;
        if (seatCount > remainingSeats) {
            return ReservationResult.rejected(remainingSeats);
        }
        bookedSeats += seatCount;
        return ReservationResult.accepted(capacity - bookedSeats);
    }

    public record ReservationResult(boolean accepted, int remainingSeats) {

        static ReservationResult accepted(int remainingSeats) {
            return new ReservationResult(true, remainingSeats);
        }

        static ReservationResult rejected(int remainingSeats) {
            return new ReservationResult(false, remainingSeats);
        }
    }
}
