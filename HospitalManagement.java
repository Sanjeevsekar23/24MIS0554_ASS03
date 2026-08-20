import java.util.*;

public class HospitalManagement {

    // Patient details
    static class Patient {
        String name;
        int age;
        boolean emergency;
        boolean insurancePatient;
        boolean followUp;

        Patient(String name, int age, boolean emergency,
                boolean insurancePatient, boolean followUp) {
            this.name = name;
            this.age = age;
            this.emergency = emergency;
            this.insurancePatient = insurancePatient;
            this.followUp = followUp;
        }
    }

    // Calculate consultation fee
    public static double calculateConsultationFee(
            Patient patient, String appointmentType, int duration) {

        double fee;

        // Base fee based on appointment type
        if (appointmentType.equalsIgnoreCase("General")) {
            fee = 500;
        } else if (appointmentType.equalsIgnoreCase("Specialist")) {
            fee = 1000;
        } else if (appointmentType.equalsIgnoreCase("Emergency")) {
            fee = 1500;
        } else {
            throw new IllegalArgumentException("Invalid appointment type");
        }

        // Extra charge for longer consultation
        if (duration > 30) {
            fee += 300;
        }

        // Emergency charge
        if (patient.emergency) {
            fee += 500;
        }

        // Senior citizen discount
        if (patient.age >= 60) {
            fee *= 0.80; // 20% discount
        }

        // Follow-up discount
        if (patient.followUp) {
            fee *= 0.50; // 50% discount
        }

        return fee;
    }

    // Calculate lab charges
    public static double calculateLabCharges(String[] labTests) {

        double total = 0;

        for (String test : labTests) {
            if (test.equalsIgnoreCase("Blood Test")) {
                total += 300;
            } else if (test.equalsIgnoreCase("X-Ray")) {
                total += 500;
            } else if (test.equalsIgnoreCase("MRI")) {
                total += 3000;
            } else if (test.equalsIgnoreCase("CT Scan")) {
                total += 2500;
            } else if (!test.equalsIgnoreCase("None")) {
                throw new IllegalArgumentException("Invalid lab test: " + test);
            }
        }

        return total;
    }

    // Calculate medicine charges
    public static double calculateMedicineCharges(
            Map<String, Integer> medicines) {

        double total = 0;

        for (Map.Entry<String, Integer> entry : medicines.entrySet()) {

            String medicine = entry.getKey();
            int quantity = entry.getValue();

            if (quantity <= 0) {
                throw new IllegalArgumentException(
                        "Medicine quantity must be positive");
            }

            double price;

            if (medicine.equalsIgnoreCase("Paracetamol")) {
                price = 10;
            } else if (medicine.equalsIgnoreCase("Antibiotic")) {
                price = 50;
            } else if (medicine.equalsIgnoreCase("Painkiller")) {
                price = 30;
            } else {
                throw new IllegalArgumentException(
                        "Invalid medicine: " + medicine);
            }

            total += price * quantity;
        }

        return total;
    }

    // Calculate insurance coverage
    public static double calculateInsuranceCoverage(
            Patient patient, double totalAmount) {

        if (!patient.insurancePatient) {
            return 0;
        }

        // Insurance covers 70%
        return totalAmount * 0.70;
    }

    // Calculate final payable amount
    public static double calculatePatientPayable(
            Patient patient,
            double consultationFee,
            double labCharges,
            double medicineCharges) {

        double totalAmount =
                consultationFee + labCharges + medicineCharges;

        double insuranceCoverage =
                calculateInsuranceCoverage(patient, totalAmount);

        return totalAmount - insuranceCoverage;
    }

    // Display hospital bill
    public static void displayBill(
            Patient patient,
            String doctor,
            String department,
            double consultationFee,
            double labCharges,
            double medicineCharges) {

        double total =
                consultationFee + labCharges + medicineCharges;

        double insuranceCoverage =
                calculateInsuranceCoverage(patient, total);

        double payable =
                total - insuranceCoverage;

        System.out.println("\n========== HOSPITAL BILL ==========");
        System.out.println("Patient Name       : " + patient.name);
        System.out.println("Age                : " + patient.age);
        System.out.println("Doctor             : " + doctor);
        System.out.println("Department         : " + department);

        System.out.printf("Consultation Fee   : %.2f%n", consultationFee);
        System.out.printf("Lab Charges        : %.2f%n", labCharges);
        System.out.printf("Medicine Charges   : %.2f%n", medicineCharges);
        System.out.printf("Total Amount       : %.2f%n", total);
        System.out.printf("Insurance Coverage : %.2f%n", insuranceCoverage);
        System.out.printf("Patient Payable    : %.2f%n", payable);

        System.out.println("===================================");
    }

    // Main method
    public static void main(String[] args) {

        Patient patient = new Patient(
                "Sanjeev",
                65,
                false,       // Emergency
                true,        // Insurance
                true         // Follow-up
        );

        String doctor = "Dr. Kumar";
        String department = "Cardiology";
        String appointmentType = "Specialist";
        int consultationDuration = 40;

        String[] labTests = {
                "Blood Test",
                "X-Ray"
        };

        Map<String, Integer> medicines = new HashMap<>();
        medicines.put("Paracetamol", 5);
        medicines.put("Antibiotic", 2);

        // Calculations
        double consultationFee =
                calculateConsultationFee(
                        patient,
                        appointmentType,
                        consultationDuration);

        double labCharges =
                calculateLabCharges(labTests);

        double medicineCharges =
                calculateMedicineCharges(medicines);

        // Display bill
        displayBill(
                patient,
                doctor,
                department,
                consultationFee,
                labCharges,
                medicineCharges);
    }
}