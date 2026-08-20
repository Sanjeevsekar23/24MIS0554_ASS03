/**
 * Built-in quality assurance tests for LoanProcessingSystem.
 */
public class LoanProcessingQA {
    private static final double EPSILON = 0.01;
    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        LoanProcessingSystem system = new LoanProcessingSystem();

        System.out.println("=== LOAN PROCESSING QA TESTS ===");
        testAgeBoundaries(system);
        testInvalidSalary(system);
        testPoorCreditScore(system);
        testExistingLoanThreshold(system);
        testHighDebtToIncomeRatio(system);
        testEmploymentCategories(system);
        testLoanAmountBoundaries(system);
        testEMIAccuracy(system);
        testInvalidInputHandling(system);
        testExceptionHandling(system);

        System.out.println("\nQA SUMMARY: " + passed + " passed, " + failed + " failed");
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void testAgeBoundaries(LoanProcessingSystem system) {
        check("Minimum age accepted", system.validateAge(21));
        check("Maximum age accepted", system.validateAge(65));
        check("Age below minimum rejected", !system.validateAge(20));
        check("Age above maximum rejected", !system.validateAge(66));
    }

    private static void testInvalidSalary(LoanProcessingSystem system) {
        check("Zero salary rejected", !system.validateSalary(0));
        check("Salary below minimum rejected", !system.validateSalary(14999));
        check("Salary above maximum rejected", !system.validateSalary(10000001));
    }

    private static void testPoorCreditScore(LoanProcessingSystem system) {
        check("Poor credit score rejected", !system.validateCreditScore(599));
        check("Credit score at minimum accepted", system.validateCreditScore(600));
    }

    private static void testExistingLoanThreshold(LoanProcessingSystem system) {
        check("Existing loan at threshold accepted", system.validateExistingLoan(300000, 50000));
        check("Existing loan above threshold rejected", !system.validateExistingLoan(300001, 50000));
    }

    private static void testHighDebtToIncomeRatio(LoanProcessingSystem system) {
        boolean approved = system.processLoanApplication(
                1001, 30, 50000, 0, 760, "Salaried", 250000, 1);
        check("High DTI application rejected", !approved);
        check("High DTI rejection reason reported",
                system.getRejectionReason().toLowerCase().contains("debt-to-income"));
    }

    private static void testEmploymentCategories(LoanProcessingSystem system) {
        double salaried = system.calculateEligibleLoanAmount(100000, 0, 750, "Salaried");
        double selfEmployed = system.calculateEligibleLoanAmount(100000, 0, 750, "Self-Employed");
        double contractual = system.calculateEligibleLoanAmount(100000, 0, 750, "Contractual");

        check("Salaried employment category supported", salaried > 0);
        check("Self-employed eligibility is reduced", selfEmployed < salaried);
        check("Contractual eligibility is reduced further", contractual < selfEmployed);
        check("Invalid employment category rejected", !system.validateEmploymentType("Unemployed"));
    }

    private static void testLoanAmountBoundaries(LoanProcessingSystem system) {
        check("Minimum loan amount accepted", system.validateRequestedLoanAmount(100000));
        check("Maximum loan amount accepted", system.validateRequestedLoanAmount(5000000));
        check("Loan below minimum rejected", !system.validateRequestedLoanAmount(99999));
        check("Loan above maximum rejected", !system.validateRequestedLoanAmount(5000001));
    }

    private static void testEMIAccuracy(LoanProcessingSystem system) {
        double actual = system.calculateEMI(1000000, 10, 10);
        double expected = 13215.073688;
        check("EMI calculation accuracy", Math.abs(actual - expected) < EPSILON);
        check("Zero-interest EMI calculation", 
                Math.abs(system.calculateEMI(120000, 0, 1) - 10000) < EPSILON);
    }

    private static void testInvalidInputHandling(LoanProcessingSystem system) {
        check("Invalid tenure rejected", !system.validateTenure(0));
        check("Excessive tenure rejected", !system.validateTenure(31));
        check("Invalid application rejected", !system.processLoanApplication(
                1002, 30, 50000, 0, 750, "Salaried", 99999, 10));
    }

    private static void testExceptionHandling(LoanProcessingSystem system) {
        boolean exceptionCaught = false;
        try {
            system.calculateEMI(0, 10, 10);
        } catch (IllegalArgumentException exception) {
            exceptionCaught = true;
        }
        check("Invalid EMI input throws expected exception", exceptionCaught);

        exceptionCaught = false;
        try {
            system.calculateDebtToIncomeRatio(1000, 0);
        } catch (IllegalArgumentException exception) {
            exceptionCaught = true;
        }
        check("Invalid salary in DTI calculation throws exception", exceptionCaught);
    }

    private static void check(String testName, boolean result) {
        if (result) {
            passed++;
            System.out.println("PASS: " + testName);
        } else {
            failed++;
            System.out.println("FAIL: " + testName);
        }
    }
}
