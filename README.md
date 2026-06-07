# Flight Booking API

A small Spring Boot REST API for booking seats on known flights. The app uses in-memory storage only and seeds a few flights when it starts.

## Run The App

```bash
mvn spring-boot:run
```

The API starts on `http://localhost:8080`.

## Run Tests

```bash
mvn test
```

## Seeded Flights

| Flight number | Capacity |
| --- | ---: |
| `AI101` | 4 |
| `BA202` | 8 |
| `UA303` | 12 |

## Book Seats

`POST /bookings`

Request:

```json
{
  "flightNumber": "AI101",
  "passengerName": "Asha Rao",
  "passengerEmail": "asha@example.com",
  "seatCount": 2
}
```

Successful response: `201 Created`

```json
{
  "bookingReference": "3dfb9d01-9e37-4a29-ae28-5a91f0f9b048",
  "flightNumber": "AI101",
  "passengerName": "Asha Rao",
  "passengerEmail": "asha@example.com",
  "seatsBooked": 2,
  "remainingSeats": 2,
  "confirmedAt": "2026-06-07T08:00:00Z"
}
```

Unknown flight response: `404 Not Found`

```json
{
  "timestamp": "2026-06-07T08:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Flight ZZ999 does not exist",
  "path": "/bookings",
  "details": []
}
```

Not enough seats response: `409 Conflict`

```json
{
  "timestamp": "2026-06-07T08:00:00Z",
  "status": 409,
  "error": "Conflict",
  "message": "Flight AI101 has only 1 seats remaining; requested 2",
  "path": "/bookings",
  "details": []
}
```
