# Flight Booking API

A small Spring Boot service for booking seats on available flights. The data is stored in memory, so bookings reset when the app restarts.

## How To Run

```bash
mvn spring-boot:run
```

The service runs at:

```text
http://localhost:8080
```

To run tests:

```bash
mvn test
```

## Available Flights

| Flight number | Capacity |
| --- | ---: |
| `AI101` | 4 |
| `BA202` | 8 |
| `UA303` | 12 |

## Example Requests

Book seats:

```bash
curl -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "AI101",
    "passengerName": "Asha Rao",
    "passengerEmail": "asha@example.com",
    "seatCount": 2
  }'
```

Try booking again with the same email:

```bash
curl -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "AI101",
    "passengerName": "Asha Rao",
    "passengerEmail": "asha@example.com",
    "seatCount": 1
  }'
```

This returns `400 Bad Request` because the same passenger email cannot book twice.

Try booking more seats than available:

```bash
curl -X POST http://localhost:8080/bookings \
  -H "Content-Type: application/json" \
  -d '{
    "flightNumber": "BA202",
    "passengerName": "Bram Hill",
    "passengerEmail": "bram@example.com",
    "seatCount": 20
  }'
```

This returns `409 Conflict` because there are not enough seats.

## What I Would Improve With More Time

- Store flights and bookings in a real database so data is not lost after restarting the app.
- Add login or user accounts so bookings are tied to real users, not only email addresses.
- Add a booking cancellation option so seats can be released if someone changes their plan.
- Add retry protection using an idempotency key, so a slow network retry does not accidentally create another booking.
- Add clearer API documentation, such as Swagger, so anyone can test the endpoints in a browser.
- Add more real-world checks, like validating flight dates, passenger details, and seat limits per person.
