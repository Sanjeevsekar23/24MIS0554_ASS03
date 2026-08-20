import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class AirlineReservation {

    // Passenger details
    static class Passenger {
        String name;
        String passengerType; // Adult, Child, Senior

        Passenger(String name, String passengerType) {
            this.name = name;
            this.passengerType = passengerType;
        }
    }

    // Flight details
    static class Flight {
        String flightNumber;
        String source;
        String destination;
        LocalDate travelDate;
        int totalSeats;
        int availableSeats;

        Flight(String flightNumber, String source, String destination,
               LocalDate travelDate, int totalSeats) {

            if (totalSeats <= 0) {
                throw new IllegalArgumentException(
                        "Total seats must be positive");
            }

            this.flightNumber = flightNumber;
            this.source = source;
            this.destination = destination;
            this.travelDate = travelDate;
            this.totalSeats = totalSeats;
            this.availableSeats = totalSeats;
        }
    }

    // Booking details
    static class Booking {
        String bookingId;
        Flight flight;
        Passenger passenger;
        String travelClass;
        double fare;
        double baggageCharge;
        LocalDate bookingDate;

        Booking(String bookingId, Flight flight, Passenger passenger,
                String travelClass, double fare,
                double baggageCharge, LocalDate bookingDate) {

            this.bookingId = bookingId;
            this.flight = flight;
            this.passenger = passenger;
            this.travelClass = travelClass;
            this.fare = fare;
            this.baggageCharge = baggageCharge;
            this.bookingDate = bookingDate;
        }

        double getTotalAmount() {
            return fare + baggageCharge;
        }
    }

    static List<Booking> bookings = new ArrayList<>();
    static int bookingCounter = 1001;

    // ============================================================
    // 1. FLIGHT SEARCH
    // ============================================================

    public static List<Flight> searchFlights(
            List<Flight> flights,
            String source,
            String destination,
            LocalDate travelDate) {

        List<Flight> results = new ArrayList<>();

        for (Flight flight : flights) {

            if (flight.source.equalsIgnoreCase(source)
                    && flight.destination.equalsIgnoreCase(destination)
                    && flight.travelDate.equals(travelDate)
                    && flight.availableSeats > 0) {

                results.add(flight);
            }
        }

        return results;
    }

    // ============================================================
    // 2. SEAT AVAILABILITY
    // ============================================================

    public static int getAvailableSeats(Flight flight) {
        return flight.availableSeats;
    }

    // ============================================================
    // 3. BASE FARE BASED ON CLASS
    // ============================================================

    public static double getBaseFare(String travelClass) {

        if (travelClass.equalsIgnoreCase("Economy")) {
            return 5000;
        } else if (travelClass.equalsIgnoreCase("Business")) {
            return 10000;
        } else if (travelClass.equalsIgnoreCase("First Class")) {
            return 18000;
        } else {
            throw new IllegalArgumentException(
                    "Invalid travel class");
        }
    }

    // ============================================================
    // 4. DYNAMIC PRICING BASED ON AVAILABLE SEATS
    // ============================================================

    public static double applySeatBasedPricing(
            double fare, Flight flight) {

        double availabilityPercentage =
                (flight.availableSeats * 100.0)
                        / flight.totalSeats;

        // Less than or equal to 20% seats available
        if (availabilityPercentage <= 20) {
            fare *= 1.50;
        }

        // Less than or equal to 50% seats available
        else if (availabilityPercentage <= 50) {
            fare *= 1.25;
        }

        // More than 50% seats available
        else {
            fare *= 1.00;
        }

        return fare;
    }

    // ============================================================
    // 5. DYNAMIC PRICING BASED ON BOOKING DATE
    // ============================================================

    public static double applyBookingDatePricing(
            double fare,
            LocalDate bookingDate,
            LocalDate travelDate) {

        long daysBeforeTravel =
                ChronoUnit.DAYS.between(bookingDate, travelDate);

        if (daysBeforeTravel < 0) {
            throw new IllegalArgumentException(
                    "Booking date cannot be after travel date");
        }

        // Booking within 7 days
        if (daysBeforeTravel <= 7) {
            fare *= 1.30;
        }

        // Booking 8 to 30 days before travel
        else if (daysBeforeTravel <= 30) {
            fare *= 1.15;
        }

        // Booking more than 30 days before travel
        else {
            fare *= 1.00;
        }

        return fare;
    }

    // ============================================================
    // 6. PASSENGER TYPE PRICING
    // ============================================================

    public static double applyPassengerTypePricing(
            double fare,
            String passengerType) {

        if (passengerType.equalsIgnoreCase("Adult")) {
            return fare;
        }

        // Child gets 20% discount
        else if (passengerType.equalsIgnoreCase("Child")) {
            return fare * 0.80;
        }

        // Senior gets 15% discount
        else if (passengerType.equalsIgnoreCase("Senior")) {
            return fare * 0.85;
        }

        else {
            throw new IllegalArgumentException(
                    "Invalid passenger type");
        }
    }

    // ============================================================
    // 7. CALCULATE FINAL FARE
    // ============================================================

    public static double calculateFare(
            Flight flight,
            Passenger passenger,
            String travelClass,
            LocalDate bookingDate) {

        double fare = getBaseFare(travelClass);

        // Dynamic pricing based on available seats
        fare = applySeatBasedPricing(fare, flight);

        // Dynamic pricing based on booking date
        fare = applyBookingDatePricing(
                fare,
                bookingDate,
                flight.travelDate);

        // Passenger type discount
        fare = applyPassengerTypePricing(
                fare,
                passenger.passengerType);

        return fare;
    }

    // ============================================================
    // 8. BAGGAGE CHARGES
    // ============================================================

    public static double calculateBaggageCharges(
            String travelClass,
            double baggageWeight) {

        if (baggageWeight < 0) {
            throw new IllegalArgumentException(
                    "Baggage weight cannot be negative");
        }

        double freeBaggage;

        if (travelClass.equalsIgnoreCase("Economy")) {
            freeBaggage = 15;
        } else if (travelClass.equalsIgnoreCase("Business")) {
            freeBaggage = 30;
        } else if (travelClass.equalsIgnoreCase("First Class")) {
            freeBaggage = 40;
        } else {
            throw new IllegalArgumentException(
                    "Invalid travel class");
        }

        if (baggageWeight <= freeBaggage) {
            return 0;
        }

        double extraWeight = baggageWeight - freeBaggage;

        // ₹500 per extra kg
        return extraWeight * 500;
    }

    // ============================================================
    // 9. PASSENGER BOOKING
    // ============================================================

    public static Booking bookPassenger(
            Flight flight,
            Passenger passenger,
            String travelClass,
            double baggageWeight,
            LocalDate bookingDate) {

        if (flight.availableSeats <= 0) {
            throw new IllegalArgumentException(
                    "No seats available");
        }

        if (bookingDate.isAfter(flight.travelDate)) {
            throw new IllegalArgumentException(
                    "Booking date cannot be after travel date");
        }

        double fare = calculateFare(
                flight,
                passenger,
                travelClass,
                bookingDate);

        double baggageCharge =
                calculateBaggageCharges(
                        travelClass,
                        baggageWeight);

        String bookingId =
                "BKG" + bookingCounter++;

        Booking booking = new Booking(
                bookingId,
                flight,
                passenger,
                travelClass,
                fare,
                baggageCharge,
                bookingDate);

        // Reduce available seats
        flight.availableSeats--;

        bookings.add(booking);

        return booking;
    }

    // ============================================================
    // 10. CANCELLATION
    // ============================================================

    public static double cancelBooking(
            Booking booking,
            LocalDate cancellationDate) {

        if (!bookings.contains(booking)) {
            throw new IllegalArgumentException(
                    "Booking not found");
        }

        long daysBeforeTravel =
                ChronoUnit.DAYS.between(
                        cancellationDate,
                        booking.flight.travelDate);

        if (daysBeforeTravel < 0) {
            throw new IllegalArgumentException(
                    "Cancellation date cannot be after travel date");
        }

        double totalAmount =
                booking.getTotalAmount();

        double refund;

        // More than 7 days before travel:
        // 90% refund
        if (daysBeforeTravel > 7) {
            refund = totalAmount * 0.90;
        }

        // 1 to 7 days before travel:
        // 70% refund
        else if (daysBeforeTravel >= 1) {
            refund = totalAmount * 0.70;
        }

        // On travel date:
        // No refund
        else {
            refund = 0;
        }

        // Return seat to availability
        booking.flight.availableSeats++;

        bookings.remove(booking);

        return refund;
    }

    // ============================================================
    // 11. DISPLAY BOOKING
    // ============================================================

    public static void displayBooking(Booking booking) {

        System.out.println("\n======================================");
        System.out.println("        AIRLINE RESERVATION");
        System.out.println("======================================");

        System.out.println("Booking ID       : " + booking.bookingId);
        System.out.println("Passenger        : "
                + booking.passenger.name);
        System.out.println("Passenger Type   : "
                + booking.passenger.passengerType);

        System.out.println("Flight           : "
                + booking.flight.flightNumber);

        System.out.println("Route            : "
                + booking.flight.source + " -> "
                + booking.flight.destination);

        System.out.println("Travel Date      : "
                + booking.flight.travelDate);

        System.out.println("Class            : "
                + booking.travelClass);

        System.out.printf(
                "Flight Fare      : %.2f%n",
                booking.fare);

        System.out.printf(
                "Baggage Charge   : %.2f%n",
                booking.baggageCharge);

        System.out.printf(
                "Total Amount     : %.2f%n",
                booking.getTotalAmount());

        System.out.println("Seats Available  : "
                + booking.flight.availableSeats);

        System.out.println("======================================");
    }

    // ============================================================
    // MAIN METHOD
    // ============================================================

    public static void main(String[] args) {

        List<Flight> flights = new ArrayList<>();

        Flight flight1 = new Flight(
                "AI101",
                "Chennai",
                "Delhi",
                LocalDate.of(2026, 9, 15),
                100);

        Flight flight2 = new Flight(
                "AI202",
                "Chennai",
                "Mumbai",
                LocalDate.of(2026, 9, 20),
                80);

        flights.add(flight1);
        flights.add(flight2);

        // Flight search
        List<Flight> results =
                searchFlights(
                        flights,
                        "Chennai",
                        "Delhi",
                        LocalDate.of(2026, 9, 15));

        System.out.println("Flights Found: "
                + results.size());

        // Passenger
        Passenger passenger =
                new Passenger(
                        "Sanjeev",
                        "Adult");

        // Booking
        Booking booking =
                bookPassenger(
                        flight1,
                        passenger,
                        "Economy",
                        20,
                        LocalDate.of(2026, 8, 20));

        displayBooking(booking);

        // Cancellation example
        double refund =
                cancelBooking(
                        booking,
                        LocalDate.of(2026, 8, 25));

        System.out.printf(
                "\nRefund Amount: %.2f%n",
                refund);

        System.out.println(
                "Available Seats After Cancellation: "
                        + flight1.availableSeats);
    }
}