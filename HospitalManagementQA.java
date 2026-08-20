import java.util.HashMap;
import java.util.Map;

public class HospitalManagementQA {

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

    // Check exception
    static void checkException(String testName, Runnable test) {
        try {
            test.run();
            System.out.println("FAIL: " + testName);
            System.out.println("      Expected IllegalArgumentException");
            failed++;
        } catch (IllegalArgumentException e) {
            System.out.println("PASS: " + testName);
            System.out.println("      Correct exception thrown");
            passed++;
        }
    }

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("   HOSPITAL MANAGEMENT QA TESTING");
        System.out.println("========================================");

        // 1. Normal patient - General appointment
        HospitalManagement.Patient patient1 =
                new HospitalManagement.Patient(
                        "Arun", 30, false, false, false);

        double fee1 =
                HospitalManagement.calculateConsultationFee(
                        patient1, "General", 30);

        check("1. Normal Patient - General Appointment",
                fee1, 500);


        // 2. Specialist appointment
        HospitalManagement.Patient patient2 =
                new HospitalManagement.Patient(
                        "Rahul", 35, false, false, false);

        double fee2 =
                HospitalManagement.calculateConsultationFee(
                        patient2, "Specialist", 30);

        check("2. Specialist Appointment",
                fee2, 1000);


        // 3. Emergency appointment
        HospitalManagement.Patient patient3 =
                new HospitalManagement.Patient(
                        "Kiran", 40, true, false, false);

        double fee3 =
                HospitalManagement.calculateConsultationFee(
                        patient3, "Emergency", 30);

        // 1500 + 500 emergency = 2000
        check("3. Emergency Appointment",
                fee3, 2000);


        // 4. Long consultation
        HospitalManagement.Patient patient4 =
                new HospitalManagement.Patient(
                        "Vijay", 40, false, false, false);

        double fee4 =
                HospitalManagement.calculateConsultationFee(
                        patient4, "General", 45);

        // 500 + 300 = 800
        check("4. Long Consultation",
                fee4, 800);


        // 5. Senior citizen discount
        HospitalManagement.Patient patient5 =
                new HospitalManagement.Patient(
                        "Ravi", 65, false, false, false);

        double fee5 =
                HospitalManagement.calculateConsultationFee(
                        patient5, "General", 30);

        // 500 × 80% = 400
        check("5. Senior Citizen Discount",
                fee5, 400);


        // 6. Follow-up consultation
        HospitalManagement.Patient patient6 =
                new HospitalManagement.Patient(
                        "Manoj", 40, false, false, true);

        double fee6 =
                HospitalManagement.calculateConsultationFee(
                        patient6, "General", 30);

        // 500 × 50% = 250
        check("6. Follow-up Consultation",
                fee6, 250);


        // 7. Senior citizen + follow-up
        HospitalManagement.Patient patient7 =
                new HospitalManagement.Patient(
                        "Suresh", 70, false, false, true);

        double fee7 =
                HospitalManagement.calculateConsultationFee(
                        patient7, "Specialist", 30);

        // 1000 × 80% × 50% = 400
        check("7. Senior + Follow-up",
                fee7, 400);


        // 8. Emergency + senior citizen
        HospitalManagement.Patient patient8 =
                new HospitalManagement.Patient(
                        "Mohan", 65, true, false, false);

        double fee8 =
                HospitalManagement.calculateConsultationFee(
                        patient8, "Emergency", 30);

        // (1500 + 500) × 80% = 1600
        check("8. Emergency + Senior Citizen",
                fee8, 1600);


        // 9. Emergency + long consultation
        HospitalManagement.Patient patient9 =
                new HospitalManagement.Patient(
                        "Ajay", 40, true, false, false);

        double fee9 =
                HospitalManagement.calculateConsultationFee(
                        patient9, "Emergency", 45);

        // 1500 + 300 + 500 = 2300
        check("9. Emergency + Long Consultation",
                fee9, 2300);


        // 10. Blood test
        String[] tests10 = {"Blood Test"};

        double lab10 =
                HospitalManagement.calculateLabCharges(tests10);

        check("10. Blood Test",
                lab10, 300);


        // 11. Multiple lab tests
        String[] tests11 = {
                "Blood Test",
                "X-Ray",
                "MRI"
        };

        double lab11 =
                HospitalManagement.calculateLabCharges(tests11);

        // 300 + 500 + 3000 = 3800
        check("11. Multiple Lab Tests",
                lab11, 3800);


        // 12. No lab tests
        String[] tests12 = {"None"};

        double lab12 =
                HospitalManagement.calculateLabCharges(tests12);

        check("12. No Lab Tests",
                lab12, 0);


        // 13. Single medicine
        Map<String, Integer> medicines13 =
                new HashMap<>();

        medicines13.put("Paracetamol", 5);

        double medicine13 =
                HospitalManagement.calculateMedicineCharges(
                        medicines13);

        check("13. Single Medicine",
                medicine13, 50);


        // 14. Multiple medicines
        Map<String, Integer> medicines14 =
                new HashMap<>();

        medicines14.put("Paracetamol", 5);
        medicines14.put("Antibiotic", 2);
        medicines14.put("Painkiller", 3);

        double medicine14 =
                HospitalManagement.calculateMedicineCharges(
                        medicines14);

        // 50 + 100 + 90 = 240
        check("14. Multiple Medicines",
                medicine14, 240);


        // 15. Insurance patient
        HospitalManagement.Patient patient15 =
                new HospitalManagement.Patient(
                        "Ramesh", 40, false, true, false);

        double coverage15 =
                HospitalManagement.calculateInsuranceCoverage(
                        patient15, 10000);

        // 70% of 10000 = 7000
        check("15. Insurance Coverage",
                coverage15, 7000);


        // 16. Non-insurance patient
        HospitalManagement.Patient patient16 =
                new HospitalManagement.Patient(
                        "Dinesh", 40, false, false, false);

        double coverage16 =
                HospitalManagement.calculateInsuranceCoverage(
                        patient16, 10000);

        check("16. No Insurance",
                coverage16, 0);


        // 17. Final bill without insurance
        HospitalManagement.Patient patient17 =
                new HospitalManagement.Patient(
                        "Amit", 30, false, false, false);

        double payable17 =
                HospitalManagement.calculatePatientPayable(
                        patient17,
                        500,
                        300,
                        200);

        // 500 + 300 + 200 = 1000
        check("17. Final Bill Without Insurance",
                payable17, 1000);


        // 18. Final bill with insurance
        HospitalManagement.Patient patient18 =
                new HospitalManagement.Patient(
                        "Karthik", 30, false, true, false);

        double payable18 =
                HospitalManagement.calculatePatientPayable(
                        patient18,
                        1000,
                        2000,
                        1000);

        // Total = 4000
        // Insurance = 2800
        // Payable = 1200
        check("18. Final Bill With Insurance",
                payable18, 1200);


        // 19. Senior + insurance
        HospitalManagement.Patient patient19 =
                new HospitalManagement.Patient(
                        "Krishna", 65, false, true, false);

        double consultation19 =
                HospitalManagement.calculateConsultationFee(
                        patient19, "Specialist", 30);

        // 1000 × 80% = 800
        check("19. Senior Insurance - Consultation",
                consultation19, 800);

        double payable19 =
                HospitalManagement.calculatePatientPayable(
                        patient19,
                        consultation19,
                        1000,
                        500);

        // Total = 2300
        // Insurance = 1610
        // Payable = 690
        check("19. Senior Insurance - Payable",
                payable19, 690);


        // 20. Emergency + insurance
        HospitalManagement.Patient patient20 =
                new HospitalManagement.Patient(
                        "Vasanth", 40, true, true, false);

        double consultation20 =
                HospitalManagement.calculateConsultationFee(
                        patient20, "Emergency", 30);

        // 1500 + 500 = 2000
        check("20. Emergency Insurance - Consultation",
                consultation20, 2000);

        double payable20 =
                HospitalManagement.calculatePatientPayable(
                        patient20,
                        consultation20,
                        1000,
                        500);

        // Total = 3500
        // Insurance = 2450
        // Payable = 1050
        check("20. Emergency Insurance - Payable",
                payable20, 1050);


        // 21. Invalid appointment type
        HospitalManagement.Patient patient21 =
                new HospitalManagement.Patient(
                        "Arun", 30, false, false, false);

        checkException(
                "21. Invalid Appointment Type",
                () -> HospitalManagement.calculateConsultationFee(
                        patient21, "Invalid", 30)
        );


        // 22. Invalid lab test
        String[] tests22 = {"Invalid Test"};

        checkException(
                "22. Invalid Lab Test",
                () -> HospitalManagement.calculateLabCharges(
                        tests22)
        );


        // 23. Invalid medicine
        Map<String, Integer> medicines23 =
                new HashMap<>();

        medicines23.put("Unknown Medicine", 2);

        checkException(
                "23. Invalid Medicine",
                () -> HospitalManagement.calculateMedicineCharges(
                        medicines23)
        );


        // 24. Zero medicine quantity
        Map<String, Integer> medicines24 =
                new HashMap<>();

        medicines24.put("Paracetamol", 0);

        checkException(
                "24. Zero Medicine Quantity",
                () -> HospitalManagement.calculateMedicineCharges(
                        medicines24)
        );


        // 25. Negative medicine quantity
        Map<String, Integer> medicines25 =
                new HashMap<>();

        medicines25.put("Paracetamol", -5);

        checkException(
                "25. Negative Medicine Quantity",
                () -> HospitalManagement.calculateMedicineCharges(
                        medicines25)
        );


        // FINAL RESULT
        System.out.println();
        System.out.println("========================================");
        System.out.println("             TEST SUMMARY");
        System.out.println("========================================");
        System.out.println("Tests Passed : " + passed);
        System.out.println("Tests Failed : " + failed);
        System.out.println("Total Tests  : " + (passed + failed));

        if (failed == 0) {
            System.out.println("ALL TESTS PASSED");
        } else {
            System.out.println("SOME TESTS FAILED");
        }

        System.out.println("========================================");

        // Make Jenkins build fail if any test fails
        if (failed > 0) {
            System.exit(1);
        }
    }
}