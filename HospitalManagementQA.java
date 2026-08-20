import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class HospitalManagementQA {

    // 1. Normal patient - General appointment
    @Test
    void testNormalPatient() {
        HospitalManagement.Patient patient =
                new HospitalManagement.Patient(
                        "Arun", 30, false, false, false);

        double fee = HospitalManagement.calculateConsultationFee(
                patient, "General", 30);

        assertEquals(500, fee, 0.01);
    }

    // 2. Specialist appointment
    @Test
    void testSpecialistAppointment() {
        HospitalManagement.Patient patient =
                new HospitalManagement.Patient(
                        "Rahul", 35, false, false, false);

        double fee = HospitalManagement.calculateConsultationFee(
                patient, "Specialist", 30);

        assertEquals(1000, fee, 0.01);
    }

    // 3. Emergency appointment
    @Test
    void testEmergencyAppointment() {
        HospitalManagement.Patient patient =
                new HospitalManagement.Patient(
                        "Kiran", 40, true, false, false);

        double fee = HospitalManagement.calculateConsultationFee(
                patient, "Emergency", 30);

        assertEquals(2000, fee, 0.01);
    }

    // 4. Long consultation
    @Test
    void testLongConsultation() {
        HospitalManagement.Patient patient =
                new HospitalManagement.Patient(
                        "Vijay", 40, false, false, false);

        double fee = HospitalManagement.calculateConsultationFee(
                patient, "General", 45);

        assertEquals(800, fee, 0.01);
    }

    // 5. Senior citizen discount
    @Test
    void testSeniorCitizen() {
        HospitalManagement.Patient patient =
                new HospitalManagement.Patient(
                        "Ravi", 65, false, false, false);

        double fee = HospitalManagement.calculateConsultationFee(
                patient, "General", 30);

        assertEquals(400, fee, 0.01);
    }

    // 6. Follow-up consultation
    @Test
    void testFollowUpConsultation() {
        HospitalManagement.Patient patient =
                new HospitalManagement.Patient(
                        "Manoj", 40, false, false, true);

        double fee = HospitalManagement.calculateConsultationFee(
                patient, "General", 30);

        assertEquals(250, fee, 0.01);
    }

    // 7. Senior citizen + follow-up
    @Test
    void testSeniorFollowUp() {
        HospitalManagement.Patient patient =
                new HospitalManagement.Patient(
                        "Suresh", 70, false, false, true);

        double fee = HospitalManagement.calculateConsultationFee(
                patient, "Specialist", 30);

        assertEquals(400, fee, 0.01);
    }

    // 8. Emergency + senior citizen
    @Test
    void testEmergencySeniorCitizen() {
        HospitalManagement.Patient patient =
                new HospitalManagement.Patient(
                        "Mohan", 65, true, false, false);

        double fee = HospitalManagement.calculateConsultationFee(
                patient, "Emergency", 30);

        assertEquals(1600, fee, 0.01);
    }

    // 9. Emergency + long consultation
    @Test
    void testEmergencyLongConsultation() {
        HospitalManagement.Patient patient =
                new HospitalManagement.Patient(
                        "Ajay", 40, true, false, false);

        double fee = HospitalManagement.calculateConsultationFee(
                patient, "Emergency", 45);

        assertEquals(2300, fee, 0.01);
    }

    // 10. Blood test
    @Test
    void testBloodTest() {
        String[] tests = {"Blood Test"};

        double charges =
                HospitalManagement.calculateLabCharges(tests);

        assertEquals(300, charges, 0.01);
    }

    // 11. Multiple lab tests
    @Test
    void testMultipleLabTests() {
        String[] tests = {
                "Blood Test",
                "X-Ray",
                "MRI"
        };

        double charges =
                HospitalManagement.calculateLabCharges(tests);

        assertEquals(3800, charges, 0.01);
    }

    // 12. No lab tests
    @Test
    void testNoLabTests() {
        String[] tests = {"None"};

        double charges =
                HospitalManagement.calculateLabCharges(tests);

        assertEquals(0, charges, 0.01);
    }

    // 13. Single medicine
    @Test
    void testSingleMedicine() {
        Map<String, Integer> medicines = new HashMap<>();
        medicines.put("Paracetamol", 5);

        double charges =
                HospitalManagement.calculateMedicineCharges(medicines);

        assertEquals(50, charges, 0.01);
    }

    // 14. Multiple medicines
    @Test
    void testMultipleMedicines() {
        Map<String, Integer> medicines = new HashMap<>();

        medicines.put("Paracetamol", 5);
        medicines.put("Antibiotic", 2);
        medicines.put("Painkiller", 3);

        double charges =
                HospitalManagement.calculateMedicineCharges(medicines);

        assertEquals(250, charges, 0.01);
    }

    // 15. Insurance patient
    @Test
    void testInsuranceCoverage() {
        HospitalManagement.Patient patient =
                new HospitalManagement.Patient(
                        "Ramesh", 40, false, true, false);

        double coverage =
                HospitalManagement.calculateInsuranceCoverage(
                        patient, 10000);

        assertEquals(7000, coverage, 0.01);
    }

    // 16. Non-insurance patient
    @Test
    void testNoInsurance() {
        HospitalManagement.Patient patient =
                new HospitalManagement.Patient(
                        "Dinesh", 40, false, false, false);

        double coverage =
                HospitalManagement.calculateInsuranceCoverage(
                        patient, 10000);

        assertEquals(0, coverage, 0.01);
    }

    // 17. Final bill without insurance
    @Test
    void testFinalBillWithoutInsurance() {
        HospitalManagement.Patient patient =
                new HospitalManagement.Patient(
                        "Amit", 30, false, false, false);

        double payable =
                HospitalManagement.calculatePatientPayable(
                        patient,
                        500,
                        300,
                        200);

        assertEquals(1000, payable, 0.01);
    }

    // 18. Final bill with insurance
    @Test
    void testFinalBillWithInsurance() {
        HospitalManagement.Patient patient =
                new HospitalManagement.Patient(
                        "Karthik", 30, false, true, false);

        double payable =
                HospitalManagement.calculatePatientPayable(
                        patient,
                        1000,
                        2000,
                        1000);

        // Total = 4000
        // Insurance = 70% = 2800
        // Payable = 1200

        assertEquals(1200, payable, 0.01);
    }

    // 19. Senior + insurance
    @Test
    void testSeniorInsurancePatient() {
        HospitalManagement.Patient patient =
                new HospitalManagement.Patient(
                        "Krishna", 65, false, true, false);

        double consultation =
                HospitalManagement.calculateConsultationFee(
                        patient, "Specialist", 30);

        assertEquals(800, consultation, 0.01);

        double payable =
                HospitalManagement.calculatePatientPayable(
                        patient,
                        consultation,
                        1000,
                        500);

        // Total = 2300
        // Insurance = 1610
        // Payable = 690

        assertEquals(690, payable, 0.01);
    }

    // 20. Emergency + insurance
    @Test
    void testEmergencyInsurancePatient() {
        HospitalManagement.Patient patient =
                new HospitalManagement.Patient(
                        "Vasanth", 40, true, true, false);

        double consultation =
                HospitalManagement.calculateConsultationFee(
                        patient, "Emergency", 30);

        assertEquals(2000, consultation, 0.01);

        double payable =
                HospitalManagement.calculatePatientPayable(
                        patient,
                        consultation,
                        1000,
                        500);

        // Total = 3500
        // Insurance = 2450
        // Payable = 1050

        assertEquals(1050, payable, 0.01);
    }

    // 21. Invalid appointment type
    @Test
    void testInvalidAppointmentType() {
        HospitalManagement.Patient patient =
                new HospitalManagement.Patient(
                        "Arun", 30, false, false, false);

        assertThrows(
                IllegalArgumentException.class,
                () -> HospitalManagement.calculateConsultationFee(
                        patient, "Invalid", 30)
        );
    }

    // 22. Invalid lab test
    @Test
    void testInvalidLabTest() {
        String[] tests = {"Invalid Test"};

        assertThrows(
                IllegalArgumentException.class,
                () -> HospitalManagement.calculateLabCharges(tests)
        );
    }

    // 23. Invalid medicine
    @Test
    void testInvalidMedicine() {
        Map<String, Integer> medicines = new HashMap<>();
        medicines.put("Unknown Medicine", 2);

        assertThrows(
                IllegalArgumentException.class,
                () -> HospitalManagement.calculateMedicineCharges(
                        medicines)
        );
    }

    // 24. Zero medicine quantity
    @Test
    void testZeroMedicineQuantity() {
        Map<String, Integer> medicines = new HashMap<>();
        medicines.put("Paracetamol", 0);

        assertThrows(
                IllegalArgumentException.class,
                () -> HospitalManagement.calculateMedicineCharges(
                        medicines)
        );
    }

    // 25. Negative medicine quantity
    @Test
    void testNegativeMedicineQuantity() {
        Map<String, Integer> medicines = new HashMap<>();
        medicines.put("Paracetamol", -5);

        assertThrows(
                IllegalArgumentException.class,
                () -> HospitalManagement.calculateMedicineCharges(
                        medicines)
        );
    }
}