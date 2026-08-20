import java.time.LocalDateTime;

public class ParkingQA {

    static int passed = 0;
    static int failed = 0;

    // ============================================================
    // CHECK DOUBLE VALUE
    // ============================================================

    static void check(
            String testName,
            double actual,
            double expected) {

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

    // ============================================================
    // CHECK INTEGER
    // ============================================================

    static void checkInt(
            String testName,
            int actual,
            int expected) {

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

    // ============================================================
    // CHECK BOOLEAN
    // ============================================================

    static void checkBoolean(
            String testName,
            boolean actual,
            boolean expected) {

        if (actual == expected) {
            System.out.println("PASS: " + testName);
            passed++;
        } else {
            System.out.println("FAIL: " + testName);
            System.out.println("      Expected: " + expected);
            System.out.println("      Actual  : " + actual);
            failed++;
        }
    }

    // ============================================================
    // CHECK OBJECT
    // ============================================================

    static void checkNotNull(
            String testName,
            Object object) {

        if (object != null) {
            System.out.println("PASS: " + testName);
            passed++;
        } else {
            System.out.println("FAIL: " + testName);
            failed++;
        }
    }

    // ============================================================
    // CHECK EXCEPTION
    // ============================================================

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

    // ============================================================
    // MAIN QA
    // ============================================================

    public static void main(String[] args) {

        System.out.println(
                "==========================================");

        System.out.println(
                "       SMART PARKING QA TESTING");

        System.out.println(
                "==========================================");


        // ========================================================
        // 1. FULL PARKING LOT
        // ========================================================

        ParkingManagement.slots.clear();
        ParkingManagement.activeVehicles.clear();

        ParkingManagement.addSlot(
                "C01", "Car", false);

        ParkingManagement.Vehicle car1 =
                new ParkingManagement.Vehicle(
                        "TN01AA1111",
                        "Car",
                        false);

        ParkingManagement.vehicleEntry(
                car1,
                LocalDateTime.of(
                        2026, 8, 20, 10, 0));

        checkInt(
                "1. Full Parking Lot",
                ParkingManagement.getAvailableSlots(),
                0);

        ParkingManagement.Vehicle car2 =
                new ParkingManagement.Vehicle(
                        "TN01AA2222",
                        "Car",
                        false);

        checkException(
                "1. Booking When Parking Is Full",
                () -> ParkingManagement.vehicleEntry(
                        car2,
                        LocalDateTime.of(
                                2026, 8, 20, 10, 0))
        );


        // ========================================================
        // 2. WRONG VEHICLE-SLOT COMBINATION
        // ========================================================

        ParkingManagement.slots.clear();
        ParkingManagement.activeVehicles.clear();

        ParkingManagement.addSlot(
                "B01", "Bike", false);

        ParkingManagement.Vehicle car3 =
                new ParkingManagement.Vehicle(
                        "TN01BB3333",
                        "Car",
                        false);

        checkException(
                "2. Wrong Vehicle-Slot Combination",
                () -> ParkingManagement.vehicleEntry(
                        car3,
                        LocalDateTime.of(
                                2026, 8, 20, 11, 0))
        );


        // ========================================================
        // 3. DUPLICATE VEHICLE
        // ========================================================

        ParkingManagement.slots.clear();
        ParkingManagement.activeVehicles.clear();

        ParkingManagement.addSlot(
                "C02", "Car", false);

        ParkingManagement.addSlot(
                "C03", "Car", false);

        ParkingManagement.Vehicle duplicateCar =
                new ParkingManagement.Vehicle(
                        "TN01CC4444",
                        "Car",
                        false);

        ParkingManagement.vehicleEntry(
                duplicateCar,
                LocalDateTime.of(
                        2026, 8, 20, 12, 0));

        checkException(
                "3. Duplicate Vehicle Entry",
                () -> ParkingManagement.vehicleEntry(
                        duplicateCar,
                        LocalDateTime.of(
                                2026, 8, 20, 13, 0))
        );


        // ========================================================
        // 4. LOST TICKET
        // ========================================================

        ParkingManagement.slots.clear();
        ParkingManagement.activeVehicles.clear();

        ParkingManagement.addSlot(
                "C04", "Car", false);

        ParkingManagement.Vehicle lostTicketCar =
                new ParkingManagement.Vehicle(
                        "TN01DD5555",
                        "Car",
                        false);

        ParkingManagement.vehicleEntry(
                lostTicketCar,
                LocalDateTime.of(
                        2026, 8, 20, 9, 0));

        double lostTicketFee =
                ParkingManagement.lostTicketExit(
                        "TN01DD5555");

        // Car rate = ₹50
        // Lost ticket = 1000 + (50 × 2)
        // = ₹1100

        check(
                "4. Lost Ticket Fee",
                lostTicketFee,
                1100);

        checkInt(
                "4. Slot Released After Lost Ticket",
                ParkingManagement.getAvailableSlots(),
                1);


        // ========================================================
        // 5. EARLY EXIT
        // ========================================================

        ParkingManagement.slots.clear();
        ParkingManagement.activeVehicles.clear();

        ParkingManagement.addSlot(
                "B02", "Bike", false);

        ParkingManagement.Vehicle bike =
                new ParkingManagement.Vehicle(
                        "TN01EE6666",
                        "Bike",
                        false);

        ParkingManagement.vehicleEntry(
                bike,
                LocalDateTime.of(
                        2026, 8, 20, 10, 0));

        double earlyExitFee =
                ParkingManagement.vehicleExit(
                        "TN01EE6666",
                        LocalDateTime.of(
                                2026, 8, 20, 10, 30));

        // Less than one hour
        // Minimum charge = 1 hour
        // Bike = ₹20/hour

        check(
                "5. Early Exit Minimum Charge",
                earlyExitFee,
                20);


        // ========================================================
        // 6. OVERNIGHT PARKING
        // ========================================================

        ParkingManagement.slots.clear();
        ParkingManagement.activeVehicles.clear();

        ParkingManagement.addSlot(
                "C05", "Car", false);

        ParkingManagement.Vehicle overnightCar =
                new ParkingManagement.Vehicle(
                        "TN01FF7777",
                        "Car",
                        false);

        ParkingManagement.vehicleEntry(
                overnightCar,
                LocalDateTime.of(
                        2026, 8, 20, 22, 0));

        double overnightFee =
                ParkingManagement.vehicleExit(
                        "TN01FF7777",
                        LocalDateTime.of(
                                2026, 8, 21, 6, 0));

        // 8 hours × ₹50 = ₹400

        check(
                "6. Overnight Parking Fee",
                overnightFee,
                400);


        // ========================================================
        // 7. PEAK-HOUR PRICING
        // ========================================================

        ParkingManagement.Vehicle peakCar =
                new ParkingManagement.Vehicle(
                        "TN01GG8888",
                        "Car",
                        false);

        double peakFee =
                ParkingManagement.calculateParkingFee(
                        peakCar,
                        LocalDateTime.of(
                                2026, 8, 20, 18, 0),
                        LocalDateTime.of(
                                2026, 8, 20, 19, 0));

        // Car = ₹50
        // Peak = 50% increase
        // ₹50 × 1.50 = ₹75

        check(
                "7. Peak-Hour Pricing",
                peakFee,
                75);


        // ========================================================
        // 8. NORMAL-HOUR PRICING
        // ========================================================

        ParkingManagement.Vehicle normalCar =
                new ParkingManagement.Vehicle(
                        "TN01HH9999",
                        "Car",
                        false);

        double normalFee =
                ParkingManagement.calculateParkingFee(
                        normalCar,
                        LocalDateTime.of(
                                2026, 8, 20, 14, 0),
                        LocalDateTime.of(
                                2026, 8, 20, 15, 0));

        check(
                "8. Normal-Hour Parking Fee",
                normalFee,
                50);


        // ========================================================
        // 9. EV CHARGING FEE
        // ========================================================

        double chargingFee =
                ParkingManagement.calculateEVChargingFee(
                        "Electric Vehicle",
                        10);

        // 10 units × ₹15 = ₹150

        check(
                "9. EV Charging Fee",
                chargingFee,
                150);


        // ========================================================
        // 10. INVALID EV CHARGING
        // ========================================================

        checkException(
                "10. Charging Fee For Non-EV",
                () -> ParkingManagement.calculateEVChargingFee(
                        "Car",
                        10)
        );


        // ========================================================
        // 11. NEGATIVE CHARGING UNITS
        // ========================================================

        checkException(
                "11. Negative EV Charging Units",
                () -> ParkingManagement.calculateEVChargingFee(
                        "Electric Vehicle",
                        -5)
        );


        // ========================================================
        // 12. EV SLOT ALLOCATION
        // ========================================================

        ParkingManagement.slots.clear();
        ParkingManagement.activeVehicles.clear();

        ParkingManagement.addSlot(
                "E01",
                "Electric Vehicle",
                false);

        ParkingManagement.Vehicle ev =
                new ParkingManagement.Vehicle(
                        "TN01EV1234",
                        "Electric Vehicle",
                        false);

        ParkingManagement.ParkingSlot evSlot =
                ParkingManagement.vehicleEntry(
                        ev,
                        LocalDateTime.of(
                                2026, 8, 20, 12, 0));

        checkNotNull(
                "12. EV Appropriate Slot Allocation",
                evSlot);

        checkBoolean(
                "12. EV Slot Type Correct",
                evSlot.slotType.equalsIgnoreCase(
                        "Electric Vehicle"),
                true);


        // ========================================================
        // 13. VIP PARKING
        // ========================================================

        ParkingManagement.slots.clear();
        ParkingManagement.activeVehicles.clear();

        ParkingManagement.addSlot(
                "VIP01",
                "Car",
                true);

        ParkingManagement.Vehicle vipCar =
                new ParkingManagement.Vehicle(
                        "TN01VIP123",
                        "Car",
                        true);

        ParkingManagement.ParkingSlot vipSlot =
                ParkingManagement.vehicleEntry(
                        vipCar,
                        LocalDateTime.of(
                                2026, 8, 20, 14, 0));

        checkBoolean(
                "13. VIP Slot Allocation",
                vipSlot.vipSlot,
                true);


        // ========================================================
        // 14. INVALID VEHICLE TYPE
        // ========================================================

        checkException(
                "14. Invalid Vehicle Type",
                () -> new ParkingManagement.Vehicle(
                        "TN01XX1234",
                        "Bus",
                        false)
        );


        // ========================================================
        // 15. INVALID VEHICLE NUMBER
        // ========================================================

        checkException(
                "15. Empty Vehicle Number",
                () -> new ParkingManagement.Vehicle(
                        "",
                        "Car",
                        false)
        );


        // ========================================================
        // 16. INVALID EXIT VEHICLE
        // ========================================================

        checkException(
                "16. Exit Vehicle Not Found",
                () -> ParkingManagement.vehicleExit(
                        "NOTFOUND",
                        LocalDateTime.of(
                                2026, 8, 20, 15, 0))
        );


        // ========================================================
        // FINAL RESULT
        // ========================================================

        System.out.println();
        System.out.println(
                "==========================================");

        System.out.println(
                "             TEST SUMMARY");

        System.out.println(
                "==========================================");

        System.out.println(
                "Tests Passed : " + passed);

        System.out.println(
                "Tests Failed : " + failed);

        System.out.println(
                "Total Tests  : "
                + (passed + failed));

        if (failed == 0) {

            System.out.println();
            System.out.println(
                    "ALL TESTS PASSED");

        } else {

            System.out.println();
            System.out.println(
                    "SOME TESTS FAILED");
        }

        System.out.println(
                "==========================================");

        // Make Jenkins build fail if any test fails
        if (failed > 0) {
            System.exit(1);
        }
    }
}