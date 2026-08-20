import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ParkingManagement {

    // ============================================================
    // VEHICLE
    // ============================================================

    static class Vehicle {
        String vehicleNumber;
        String vehicleType;
        boolean vip;

        Vehicle(String vehicleNumber, String vehicleType, boolean vip) {

            if (vehicleNumber == null || vehicleNumber.trim().isEmpty()) {
                throw new IllegalArgumentException(
                        "Vehicle number cannot be empty");
            }

            validateVehicleType(vehicleType);

            this.vehicleNumber = vehicleNumber;
            this.vehicleType = vehicleType;
            this.vip = vip;
        }
    }

    // ============================================================
    // PARKING SLOT
    // ============================================================

    static class ParkingSlot {
        String slotId;
        String slotType;
        boolean vipSlot;
        boolean occupied;
        Vehicle vehicle;

        ParkingSlot(String slotId, String slotType, boolean vipSlot) {

            validateVehicleType(slotType);

            this.slotId = slotId;
            this.slotType = slotType;
            this.vipSlot = vipSlot;
            this.occupied = false;
            this.vehicle = null;
        }
    }

    // ============================================================
    // PARKING RECORD
    // ============================================================

    static class ParkingRecord {
        Vehicle vehicle;
        ParkingSlot slot;
        LocalDateTime entryTime;

        ParkingRecord(
                Vehicle vehicle,
                ParkingSlot slot,
                LocalDateTime entryTime) {

            this.vehicle = vehicle;
            this.slot = slot;
            this.entryTime = entryTime;
        }
    }

    static List<ParkingSlot> slots = new ArrayList<>();
    static List<ParkingRecord> activeVehicles = new ArrayList<>();

    // ============================================================
    // VALIDATE VEHICLE TYPE
    // ============================================================

    public static void validateVehicleType(String vehicleType) {

        if (vehicleType == null) {
            throw new IllegalArgumentException(
                    "Vehicle type cannot be null");
        }

        if (!(vehicleType.equalsIgnoreCase("Bike")
                || vehicleType.equalsIgnoreCase("Car")
                || vehicleType.equalsIgnoreCase("SUV")
                || vehicleType.equalsIgnoreCase("Truck")
                || vehicleType.equalsIgnoreCase("Electric Vehicle"))) {

            throw new IllegalArgumentException(
                    "Invalid vehicle type: " + vehicleType);
        }
    }

    // ============================================================
    // CREATE PARKING SLOT
    // ============================================================

    public static void addSlot(
            String slotId,
            String slotType,
            boolean vipSlot) {

        slots.add(
                new ParkingSlot(
                        slotId,
                        slotType,
                        vipSlot));
    }

    // ============================================================
    // CHECK SLOT COMPATIBILITY
    // ============================================================

    public static boolean isCompatible(
            String vehicleType,
            String slotType) {

        // Bike can only use Bike slot
        if (vehicleType.equalsIgnoreCase("Bike")) {
            return slotType.equalsIgnoreCase("Bike");
        }

        // Car can use Car slot
        if (vehicleType.equalsIgnoreCase("Car")) {
            return slotType.equalsIgnoreCase("Car");
        }

        // SUV can use SUV slot
        if (vehicleType.equalsIgnoreCase("SUV")) {
            return slotType.equalsIgnoreCase("SUV");
        }

        // Truck can use Truck slot
        if (vehicleType.equalsIgnoreCase("Truck")) {
            return slotType.equalsIgnoreCase("Truck");
        }

        // EV requires EV slot
        if (vehicleType.equalsIgnoreCase("Electric Vehicle")) {
            return slotType.equalsIgnoreCase("Electric Vehicle");
        }

        return false;
    }

    // ============================================================
    // FIND APPROPRIATE SLOT
    // ============================================================

    public static ParkingSlot findAppropriateSlot(
            Vehicle vehicle) {

        // VIP vehicle gets a VIP slot first
        if (vehicle.vip) {

            for (ParkingSlot slot : slots) {

                if (!slot.occupied
                        && slot.vipSlot
                        && isCompatible(
                                vehicle.vehicleType,
                                slot.slotType)) {

                    return slot;
                }
            }

            throw new IllegalArgumentException(
                    "No VIP parking slot available for "
                            + vehicle.vehicleType);
        }

        // Normal vehicle gets a normal slot
        for (ParkingSlot slot : slots) {

            if (!slot.occupied
                    && !slot.vipSlot
                    && isCompatible(
                            vehicle.vehicleType,
                            slot.slotType)) {

                return slot;
            }
        }

        // If normal slot is unavailable, allow non-VIP vehicle
        // to use an unused VIP slot of the correct type.
        for (ParkingSlot slot : slots) {

            if (!slot.occupied
                    && slot.vipSlot
                    && isCompatible(
                            vehicle.vehicleType,
                            slot.slotType)) {

                return slot;
            }
        }

        throw new IllegalArgumentException(
                "No suitable parking slot available");
    }

    // ============================================================
    // VEHICLE ENTRY
    // ============================================================

    public static ParkingSlot vehicleEntry(
            Vehicle vehicle,
            LocalDateTime entryTime) {

        if (entryTime == null) {
            throw new IllegalArgumentException(
                    "Entry time cannot be null");
        }

        // Prevent same vehicle from entering twice
        for (ParkingRecord record : activeVehicles) {

            if (record.vehicle.vehicleNumber
                    .equalsIgnoreCase(vehicle.vehicleNumber)) {

                throw new IllegalArgumentException(
                        "Vehicle is already parked");
            }
        }

        ParkingSlot slot =
                findAppropriateSlot(vehicle);

        slot.occupied = true;
        slot.vehicle = vehicle;

        activeVehicles.add(
                new ParkingRecord(
                        vehicle,
                        slot,
                        entryTime));

        return slot;
    }

    // ============================================================
    // PEAK HOUR CHECK
    // ============================================================

    public static boolean isPeakHour(
            LocalDateTime time) {

        LocalTime currentTime =
                time.toLocalTime();

        // Morning peak: 8:00 AM - 10:00 AM
        boolean morningPeak =
                !currentTime.isBefore(
                        LocalTime.of(8, 0))
                && currentTime.isBefore(
                        LocalTime.of(10, 0));

        // Evening peak: 5:00 PM - 8:00 PM
        boolean eveningPeak =
                !currentTime.isBefore(
                        LocalTime.of(17, 0))
                && currentTime.isBefore(
                        LocalTime.of(20, 0));

        return morningPeak || eveningPeak;
    }

    // ============================================================
    // BASE PARKING FEE
    // ============================================================

    public static double getBaseHourlyRate(
            String vehicleType) {

        if (vehicleType.equalsIgnoreCase("Bike")) {
            return 20;
        }

        if (vehicleType.equalsIgnoreCase("Car")) {
            return 50;
        }

        if (vehicleType.equalsIgnoreCase("SUV")) {
            return 70;
        }

        if (vehicleType.equalsIgnoreCase("Truck")) {
            return 100;
        }

        if (vehicleType.equalsIgnoreCase(
                "Electric Vehicle")) {

            return 40;
        }

        throw new IllegalArgumentException(
                "Invalid vehicle type");
    }

    // ============================================================
    // DYNAMIC PARKING FEE
    // ============================================================

    public static double calculateParkingFee(
            Vehicle vehicle,
            LocalDateTime entryTime,
            LocalDateTime exitTime) {

        if (entryTime == null || exitTime == null) {
            throw new IllegalArgumentException(
                    "Entry and exit time are required");
        }

        if (exitTime.isBefore(entryTime)) {
            throw new IllegalArgumentException(
                    "Exit time cannot be before entry time");
        }

        long minutes =
                Duration.between(
                        entryTime,
                        exitTime).toMinutes();

        // Minimum charge is one hour
        long hours =
                Math.max(1, (minutes + 59) / 60);

        double rate =
                getBaseHourlyRate(
                        vehicle.vehicleType);

        double fee = rate * hours;

        // Peak-hour pricing: 50% increase
        if (isPeakHour(entryTime)
                || isPeakHour(exitTime)) {

            fee *= 1.50;
        }

        // VIP gets 20% discount
        if (vehicle.vip) {
            fee *= 0.80;
        }

        return fee;
    }

    // ============================================================
    // LOST TICKET FEE
    // ============================================================

    public static double calculateLostTicketFee(
            Vehicle vehicle) {

        double baseRate =
                getBaseHourlyRate(
                        vehicle.vehicleType);

        // Fixed lost ticket penalty
        double lostTicketFee =
                1000 + baseRate * 2;

        // VIP gets a 20% discount
        if (vehicle.vip) {
            lostTicketFee *= 0.80;
        }

        return lostTicketFee;
    }

    // ============================================================
    // VEHICLE EXIT
    // ============================================================

    public static double vehicleExit(
            String vehicleNumber,
            LocalDateTime exitTime) {

        if (vehicleNumber == null
                || vehicleNumber.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "Vehicle number cannot be empty");
        }

        for (ParkingRecord record : activeVehicles) {

            if (record.vehicle.vehicleNumber
                    .equalsIgnoreCase(vehicleNumber)) {

                double fee =
                        calculateParkingFee(
                                record.vehicle,
                                record.entryTime,
                                exitTime);

                // Free the slot
                record.slot.occupied = false;
                record.slot.vehicle = null;

                // Remove active parking record
                activeVehicles.remove(record);

                return fee;
            }
        }

        throw new IllegalArgumentException(
                "Vehicle not found in parking");
    }

    // ============================================================
    // LOST TICKET HANDLING
    // ============================================================

    public static double lostTicketExit(
            String vehicleNumber) {

        for (ParkingRecord record : activeVehicles) {

            if (record.vehicle.vehicleNumber
                    .equalsIgnoreCase(vehicleNumber)) {

                double fee =
                        calculateLostTicketFee(
                                record.vehicle);

                // Free the slot
                record.slot.occupied = false;
                record.slot.vehicle = null;

                activeVehicles.remove(record);

                return fee;
            }
        }

        throw new IllegalArgumentException(
                "Vehicle not found in parking");
    }

    // ============================================================
    // GET AVAILABLE SLOTS
    // ============================================================

    public static int getAvailableSlots() {

        int count = 0;

        for (ParkingSlot slot : slots) {

            if (!slot.occupied) {
                count++;
            }
        }

        return count;
    }

    // ============================================================
    // GET AVAILABLE SLOTS BY TYPE
    // ============================================================

    public static int getAvailableSlotsByType(
            String slotType) {

        validateVehicleType(slotType);

        int count = 0;

        for (ParkingSlot slot : slots) {

            if (!slot.occupied
                    && slot.slotType.equalsIgnoreCase(
                            slotType)) {

                count++;
            }
        }

        return count;
    }

    // ============================================================
    // DISPLAY PARKING STATUS
    // ============================================================

    public static void displayParkingStatus() {

        System.out.println(
                "\n======================================");

        System.out.println(
                "       SMART PARKING STATUS");

        System.out.println(
                "======================================");

        for (ParkingSlot slot : slots) {

            System.out.println(
                    slot.slotId
                    + " | Type: "
                    + slot.slotType
                    + " | VIP: "
                    + slot.vipSlot
                    + " | Status: "
                    + (slot.occupied
                    ? "OCCUPIED"
                    : "AVAILABLE"));
        }

        System.out.println(
                "Available Slots: "
                + getAvailableSlots());

        System.out.println(
                "======================================");
    }

    // ============================================================
    // MAIN METHOD
    // ============================================================

    public static void main(String[] args) {

        // Create parking slots

        addSlot("B01", "Bike", false);
        addSlot("B02", "Bike", false);

        addSlot("C01", "Car", false);
        addSlot("C02", "Car", false);

        addSlot("S01", "SUV", false);
        addSlot("S02", "SUV", true);

        addSlot("T01", "Truck", false);

        addSlot(
                "E01",
                "Electric Vehicle",
                false);

        addSlot(
                "VIP-C01",
                "Car",
                true);

        displayParkingStatus();

        // Vehicle entry

        Vehicle car =
                new Vehicle(
                        "TN01AB1234",
                        "Car",
                        false);

        ParkingSlot allocatedSlot =
                vehicleEntry(
                        car,
                        LocalDateTime.of(
                                2026,
                                8,
                                20,
                                9,
                                0));

        System.out.println(
                "\nVehicle Entry Successful");

        System.out.println(
                "Vehicle: "
                + car.vehicleNumber);

        System.out.println(
                "Allocated Slot: "
                + allocatedSlot.slotId);

        // Vehicle exit

        double fee =
                vehicleExit(
                        "TN01AB1234",
                        LocalDateTime.of(
                                2026,
                                8,
                                20,
                                11,
                                30));

        System.out.printf(
                "Parking Fee: %.2f%n",
                fee);

        // VIP vehicle

        Vehicle vipCar =
                new Vehicle(
                        "TN02VIP999",
                        "Car",
                        true);

        ParkingSlot vipSlot =
                vehicleEntry(
                        vipCar,
                        LocalDateTime.of(
                                2026,
                                8,
                                20,
                                18,
                                0));

        System.out.println(
                "\nVIP Vehicle Entry Successful");

        System.out.println(
                "VIP Slot: "
                + vipSlot.slotId);

        // Lost ticket example

        double lostTicketFee =
                lostTicketExit(
                        "TN02VIP999");

        System.out.printf(
                "Lost Ticket Fee: %.2f%n",
                lostTicketFee);

        displayParkingStatus();
    }
}