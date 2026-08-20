import java.time.LocalDate;

public class AirlineReservationQA {

    static int passed = 0;
    static int failed = 0;

    // Check numerical result
    static void check(String testName, double actual, double expected) {

        if (Math.abs(actual - expected) < 0.01) {
            System.out.println("PASS: " + testName);
            System.out.println("      Expected: " + expected);
            System.out.println("      Actual  : " + actual);
            passed++;
        } else {
            System.out.println("FAIL: " + testName);
            System.out.println("      Expected: " + expected);
            System.out.println("      Actual  : " + actual);
            failed++;
        }
    }

    // Check integer result
    static void checkInt(String testName, int actual, int expected) {

        if (actual == expected) {
            System.out.println("PASS: " + testName);
            System.out.println("      Expected: " + expected);
            System.out.println("      Actual  : " + actual);
            passed++;
        } else {
            System.out.println("FAIL: " + testName);
            System.out.println("      Expected: " + expected);
            System.out.println("      Actual  : " + actual);
            failed++;
        }
    }

    // Check successful operation
    static void checkNotNull(String testName, Object object) {

        if (object != null) {
            System.out.println("PASS: " + testName);
            passed++;
        } else {
            System.out.println("FAIL: " + testName);
            failed++;
        }
    }

    // Check expected exception
    static void checkException(
            String testName,
            Runnable test) {

        try {
            test.run();

            System.out.println("FAIL: " + testName);
            System.out.println(
                    "      Expected IllegalArgumentException");
            failed++;

        } catch (IllegalArgumentException e) {

            System.out.println("PASS: " + testName);
            System.out.println(
                    "      Correct exception thrown");
            passed++;
        }
    }

    public static void main(String[] args) {

        System.out.println("==========================================");
        System.out.println("     AIRLINE RESERVATION QA TESTING");
        System.out.println("==========================================");


        // =====================================================
        // TEST 1: SUCCESSFUL BOOKING
        // =====================================================

        AirlineReservation.Flight flight1 =
                new AirlineReservation.Flight(
                        "AI101",
                        "Chennai",
                        "Delhi",
                        LocalDate.of(2026, 10, 15),
                        100);

        AirlineReservation.Passenger passenger1 =
                new AirlineReservation.Passenger(
                        "Arun",
                        "Adult");

        AirlineReservation.Booking booking1 =
                AirlineReservation.bookPassenger(
                        flight1,
                        passenger1,
                        "Economy",
                        10,
                        LocalDate.of(2026, 8, 20));

        checkNotNull(
                "1. Successful Booking",
                booking1);

        checkInt(
                "1. Seat Count After Booking",
                flight1.availableSeats,
                99);


        // =====================================================
        // TEST 2: DOUBLE BOOKING
        // =====================================================

        AirlineReservation.Passenger passenger2 =
                new AirlineReservation.Passenger(
                        "Rahul",
                        "Adult");

        AirlineReservation.Booking booking2 =
                AirlineReservation.bookPassenger(
                        flight1,
                        passenger2,
                        "Economy",
                        10,
                        LocalDate.of(2026, 8, 20));

        checkNotNull(
                "2. Second Passenger Booking",
                booking2);

        checkInt(
                "2. Seat Count After Second Booking",
                flight1.availableSeats,
                98);


        // =====================================================
        // TEST 3: CANCELLATION
        // =====================================================

        int seatsBeforeCancellation =
                flight1.availableSeats;

        double refund =
                AirlineReservation.cancelBooking(
                        booking2,
                        LocalDate.of(2026, 9, 1));

        // More than 7 days before travel
        // Refund = 90%
        double expectedRefund =
                booking2.getTotalAmount() * 0.90;

        check(
                "3. Cancellation Refund",
                refund,
                expectedRefund);

        checkInt(
                "3. Seat Returned After Cancellation",
                flight1.availableSeats,
                seatsBeforeCancellation + 1);


        // =====================================================
        // TEST 4: REFUND WITHIN 7 DAYS
        // =====================================================

        AirlineReservation.Passenger passenger3 =
                new AirlineReservation.Passenger(
                        "Kiran",
                        "Adult");

        AirlineReservation.Booking booking3 =
                AirlineReservation.bookPassenger(
                        flight1,
                        passenger3,
                        "Business",
                        20,
                        LocalDate.of(2026, 8, 20));

        double refundWithin7Days =
                AirlineReservation.cancelBooking(
                        booking3,
                        LocalDate.of(2026, 10, 10));

        // 5 days before travel
        double expectedRefundWithin7Days =
                booking3.getTotalAmount() * 0.70;

        check(
                "4. Refund Within 7 Days",
                refundWithin7Days,
                expectedRefundWithin7Days);


        // =====================================================
        // TEST 5: FULLY BOOKED FLIGHT
        // =====================================================

        AirlineReservation.Flight fullFlight =
                new AirlineReservation.Flight(
                        "AI999",
                        "Chennai",
                        "Bangalore",
                        LocalDate.of(2026, 11, 10),
                        1);

        AirlineReservation.Passenger passenger4 =
                new AirlineReservation.Passenger(
                        "Vijay",
                        "Adult");

        // First booking consumes the only seat
        AirlineReservation.bookPassenger(
                fullFlight,
                passenger4,
                "Economy",
                10,
                LocalDate.of(2026, 8, 20));

        checkInt(
                "5. Fully Booked Flight",
                fullFlight.availableSeats,
                0);

        // Second booking must fail
        AirlineReservation.Passenger passenger5 =
                new AirlineReservation.Passenger(
                        "Ajay",
                        "Adult");

        checkException(
                "5. Booking On Fully Booked Flight",
                () -> AirlineReservation.bookPassenger(
                        fullFlight,
                        passenger5,
                        "Economy",
                        10,
                        LocalDate.of(2026, 8, 20))
        );


        // =====================================================
        // TEST 6: INVALID PASSENGER TYPE
        // =====================================================

        AirlineReservation.Flight flight6 =
                new AirlineReservation.Flight(
                        "AI106",
                        "Chennai",
                        "Delhi",
                        LocalDate.of(2026, 10, 15),
                        100);

        AirlineReservation.Passenger invalidPassenger =
                new AirlineReservation.Passenger(
                        "Invalid User",
                        "InvalidType");

        checkException(
                "6. Invalid Passenger Type",
                () -> AirlineReservation.bookPassenger(
                        flight6,
                        invalidPassenger,
                        "Economy",
                        10,
                        LocalDate.of(2026, 8, 20))
        );


        // =====================================================
        // TEST 7: EXCESS BAGGAGE - ECONOMY
        // =====================================================

        double economyBaggage =
                AirlineReservation.calculateBaggageCharges(
                        "Economy",
                        20);

        // Free = 15 kg
        // Extra = 5 kg
        // 5 × 500 = 2500

        check(
                "7. Excess Baggage - Economy",
                economyBaggage,
                2500);


        // =====================================================
        // TEST 8: EXCESS BAGGAGE - BUSINESS
        // =====================================================

        double businessBaggage =
                AirlineReservation.calculateBaggageCharges(
                        "Business",
                        35);

        // Free = 30 kg
        // Extra = 5 kg
        // 5 × 500 = 2500

        check(
                "8. Excess Baggage - Business",
                businessBaggage,
                2500);


        // =====================================================
        // TEST 9: EXCESS BAGGAGE - FIRST CLASS
        // =====================================================

        double firstClassBaggage =
                AirlineReservation.calculateBaggageCharges(
                        "First Class",
                        45);

        // Free = 40 kg
        // Extra = 5 kg
        // 5 × 500 = 2500

        check(
                "9. Excess Baggage - First Class",
                firstClassBaggage,
                2500);


        // =====================================================
        // TEST 10: NEGATIVE BAGGAGE
        // =====================================================

        checkException(
                "10. Negative Baggage Weight",
                () -> AirlineReservation.calculateBaggageCharges(
                        "Economy",
                        -5)
        );


        // =====================================================
        // TEST 11: ECONOMY BASE FARE
        // =====================================================

        double economyFare =
                AirlineReservation.getBaseFare(
                        "Economy");

        check(
                "11. Economy Base Fare",
                economyFare,
                5000);


        // =====================================================
        // TEST 12: BUSINESS BASE FARE
        // =====================================================

        double businessFare =
                AirlineReservation.getBaseFare(
                        "Business");

        check(
                "12. Business Base Fare",
                businessFare,
                10000);


        // =====================================================
        // TEST 13: FIRST CLASS BASE FARE
        // =====================================================

        double firstFare =
                AirlineReservation.getBaseFare(
                        "First Class");

        check(
                "13. First Class Base Fare",
                firstFare,
                18000);


        // =====================================================
        // TEST 14: LOW SEAT AVAILABILITY DYNAMIC PRICING
        // =====================================================

        AirlineReservation.Flight lowSeatFlight =
                new AirlineReservation.Flight(
                        "AI200",
                        "Chennai",
                        "Delhi",
                        LocalDate.of(2026, 10, 15),
                        100);

        // Make only 20 seats available
        lowSeatFlight.availableSeats = 20;

        double lowSeatFare =
                AirlineReservation.applySeatBasedPricing(
                        5000,
                        lowSeatFlight);

        // 5000 × 1.50 = 7500

        check(
                "14. Dynamic Pricing - Low Seats",
                lowSeatFare,
                7500);


        // =====================================================
        // TEST 15: MEDIUM SEAT AVAILABILITY
        // =====================================================

        AirlineReservation.Flight mediumSeatFlight =
                new AirlineReservation.Flight(
                        "AI201",
                        "Chennai",
                        "Delhi",
                        LocalDate.of(2026, 10, 15),
                        100);

        mediumSeatFlight.availableSeats = 50;

        double mediumSeatFare =
                AirlineReservation.applySeatBasedPricing(
                        5000,
                        mediumSeatFlight);

        // 5000 × 1.25 = 6250

        check(
                "15. Dynamic Pricing - Medium Seats",
                mediumSeatFare,
                6250);


        // =====================================================
        // TEST 16: HIGH SEAT AVAILABILITY
        // =====================================================

        AirlineReservation.Flight highSeatFlight =
                new AirlineReservation.Flight(
                        "AI202",
                        "Chennai",
                        "Delhi",
                        LocalDate.of(2026, 10, 15),
                        100);

        highSeatFlight.availableSeats = 80;

        double highSeatFare =
                AirlineReservation.applySeatBasedPricing(
                        5000,
                        highSeatFlight);

        check(
                "16. Dynamic Pricing - High Seats",
                highSeatFare,
                5000);


        // =====================================================
        // TEST 17: EARLY BOOKING PRICE
        // =====================================================

        double earlyBookingFare =
                AirlineReservation.applyBookingDatePricing(
                        5000,
                        LocalDate.of(2026, 8, 20),
                        LocalDate.of(2026, 10, 15));

        // More than 30 days
        // No increase

        check(
                "17. Dynamic Pricing - Early Booking",
                earlyBookingFare,
                5000);


        // =====================================================
        // TEST 18: LAST MINUTE BOOKING PRICE
        // =====================================================

        double lastMinuteFare =
                AirlineReservation.applyBookingDatePricing(
                        5000,
                        LocalDate.of(2026, 10, 10),
                        LocalDate.of(2026, 10, 15));

        // Within 7 days
        // 5000 × 1.30 = 6500

        check(
                "18. Dynamic Pricing - Last Minute",
                lastMinuteFare,
                6500);


        // =====================================================
        // TEST 19: CHILD PASSENGER
        // =====================================================

        double childFare =
                AirlineReservation.applyPassengerTypePricing(
                        5000,
                        "Child");

        // 5000 × 80% = 4000

        check(
                "19. Child Passenger Discount",
                childFare,
                4000);


        // =====================================================
        // TEST 20: SENIOR PASSENGER
        // =====================================================

        double seniorFare =
                AirlineReservation.applyPassengerTypePricing(
                        5000,
                        "Senior");

        // 5000 × 85% = 4250

        check(
                "20. Senior Passenger Discount",
                seniorFare,
                4250);


        // =====================================================
        // TEST 21: INVALID TRAVEL CLASS
        // =====================================================

        checkException(
                "21. Invalid Travel Class",
                () -> AirlineReservation.getBaseFare(
                        "Premium")
        );


        // =====================================================
        // TEST 22: BOOKING DATE AFTER TRAVEL DATE
        // =====================================================

        AirlineReservation.Flight flight22 =
                new AirlineReservation.Flight(
                        "AI222",
                        "Chennai",
                        "Delhi",
                        LocalDate.of(2026, 10, 15),
                        100);

        AirlineReservation.Passenger passenger22 =
                new AirlineReservation.Passenger(
                        "Test User",
                        "Adult");

        checkException(
                "22. Invalid Booking Date",
                () -> AirlineReservation.bookPassenger(
                        flight22,
                        passenger22,
                        "Economy",
                        10,
                        LocalDate.of(2026, 10, 20))
        );


        // =====================================================
        // FINAL RESULT
        // =====================================================

        System.out.println();
        System.out.println("==========================================");
        System.out.println("             TEST SUMMARY");
        System.out.println("==========================================");

        System.out.println("Tests Passed : " + passed);
        System.out.println("Tests Failed : " + failed);
        System.out.println(
                "Total Tests  : " + (passed + failed));

        if (failed == 0) {
            System.out.println();
            System.out.println("ALL TESTS PASSED");
        } else {
            System.out.println();
            System.out.println("SOME TESTS FAILED");
        }

        System.out.println("==========================================");

        // Jenkins build fails if any QA test fails
        if (failed > 0) {
            System.exit(1);
        }
    }
}