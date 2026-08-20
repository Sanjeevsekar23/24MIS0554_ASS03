/**
 * Loan Processing System
 * Handles loan application processing with eligibility checks and calculations
 */
public class LoanProcessingSystem {
    
    // Constants for validation
    private static final int MIN_AGE = 21;
    private static final int MAX_AGE = 65;
    private static final double MIN_SALARY = 15000.0;
    private static final double MAX_SALARY = 10000000.0;
    private static final int MIN_CREDIT_SCORE = 300;
    private static final int MAX_CREDIT_SCORE = 900;
    private static final double MAX_DTI_RATIO = 0.42;
    private static final double MAX_EXISTING_LOAN_RATIO = 0.50;
    private static final double MIN_LOAN_AMOUNT = 100000.0;
    private static final double MAX_LOAN_AMOUNT = 5000000.0;
    private static final int MIN_TENURE = 1;
    private static final int MAX_TENURE = 30;
    
    // Interest rate brackets based on credit score
    private static final int[] CREDIT_BRACKETS = {300, 400, 500, 600, 700, 800};
    private static final double[] INTEREST_RATES = {12.5, 11.5, 10.5, 9.0, 7.5, 6.5};
    
    // Employment type multipliers for loan eligibility
    private static final String[] EMPLOYMENT_TYPES = {"Salaried", "Self-Employed", "Contractual"};
    private static final double[] EMPLOYMENT_MULTIPLIERS = {1.0, 0.85, 0.75};
    
    // Customer data
    private int customerId;
    private int age;
    private double monthlySalary;
    private double existingLoanAmount;
    private int creditScore;
    private String employmentType;
    private double requestedLoanAmount;
    private int loanTenure;
    
    // Calculated values
    private double debtToIncomeRatio;
    private double eligibleLoanAmount;
    private double interestRate;
    private double emi;
    private boolean approvalStatus;
    private String rejectionReason;
    
    public LoanProcessingSystem() {
        this.rejectionReason = "";
    }
    
    /**
     * Validates customer age
     */
    public boolean validateAge(int age) {
        if (age < MIN_AGE || age > MAX_AGE) {
            this.rejectionReason = "Age must be between " + MIN_AGE + " and " + MAX_AGE + " years";
            return false;
        }
        return true;
    }
    
    /**
     * Validates monthly salary
     */
    public boolean validateSalary(double salary) {
        if (salary <= 0 || salary < MIN_SALARY || salary > MAX_SALARY) {
            this.rejectionReason = "Invalid salary. Must be between " + MIN_SALARY + " and " + MAX_SALARY;
            return false;
        }
        return true;
    }
    
    /**
     * Validates credit score
     */
    public boolean validateCreditScore(int score) {
        if (score < MIN_CREDIT_SCORE || score > MAX_CREDIT_SCORE) {
            this.rejectionReason = "Credit score must be between " + MIN_CREDIT_SCORE + " and " + MAX_CREDIT_SCORE;
            return false;
        }
        if (score < 600) {
            this.rejectionReason = "Poor credit score. Minimum required: 600";
            return false;
        }
        return true;
    }
    
    /**
     * Validates employment type
     */
    public boolean validateEmploymentType(String type) {
        for (String employment : EMPLOYMENT_TYPES) {
            if (employment.equalsIgnoreCase(type)) {
                return true;
            }
        }
        this.rejectionReason = "Invalid employment type. Allowed: Salaried, Self-Employed, Contractual";
        return false;
    }
    
    /**
     * Validates existing loan amount against salary
     */
    public boolean validateExistingLoan(double existingLoan, double salary) {
        double annualSalary = salary * 12;
        if (existingLoan > annualSalary * MAX_EXISTING_LOAN_RATIO) {
            this.rejectionReason = "Existing loan exceeds threshold (max: 50% of annual salary)";
            return false;
        }
        return true;
    }
    
    /**
     * Validates requested loan amount
     */
    public boolean validateRequestedLoanAmount(double amount) {
        if (amount < MIN_LOAN_AMOUNT || amount > MAX_LOAN_AMOUNT) {
            this.rejectionReason = "Loan amount must be between " + MIN_LOAN_AMOUNT + " and " + MAX_LOAN_AMOUNT;
            return false;
        }
        return true;
    }
    
    /**
     * Validates loan tenure (in years)
     */
    public boolean validateTenure(int tenure) {
        if (tenure < MIN_TENURE || tenure > MAX_TENURE) {
            this.rejectionReason = "Loan tenure must be between " + MIN_TENURE + " and " + MAX_TENURE + " years";
            return false;
        }
        return true;
    }
    
    /**
     * Calculates debt-to-income ratio
     */
    public double calculateDebtToIncomeRatio(double totalMonthlyDebt, double monthlySalary) {
        if (monthlySalary <= 0) {
            throw new IllegalArgumentException("Monthly salary must be positive");
        }
        return totalMonthlyDebt / monthlySalary;
    }
    
    /**
     * Determines interest rate based on credit score
     */
    public double determineInterestRate(int creditScore) {
        for (int i = CREDIT_BRACKETS.length - 1; i >= 0; i--) {
            if (creditScore >= CREDIT_BRACKETS[i]) {
                return INTEREST_RATES[i];
            }
        }
        return INTEREST_RATES[0];
    }
    
    /**
     * Calculates eligible loan amount based on various factors
     */
    public double calculateEligibleLoanAmount(double monthlySalary, double existingLoan, 
                                             int creditScore, String employmentType) {
        double annualSalary = monthlySalary * 12;
        double maxDTILoan = (monthlySalary * MAX_DTI_RATIO * 12) - existingLoan;
        
        // Calculate based on employment type multiplier
        int employmentIndex = getEmploymentTypeIndex(employmentType);
        double employmentMultiplier = EMPLOYMENT_MULTIPLIERS[employmentIndex];
        
        double baseEligibleLoan = annualSalary * 5;
        double eligibleLoan = Math.min(baseEligibleLoan, maxDTILoan);

        eligibleLoan = eligibleLoan * employmentMultiplier;

        return Math.max(0, eligibleLoan);
    }
    
    /**
     * Gets employment type index
     */
    private int getEmploymentTypeIndex(String type) {
        for (int i = 0; i < EMPLOYMENT_TYPES.length; i++) {
            if (EMPLOYMENT_TYPES[i].equalsIgnoreCase(type)) {
                return i;
            }
        }
        return 0; // Default to Salaried
    }
    
    /**
     * Calculates EMI (Equated Monthly Installment)
     * Using formula: EMI = P * r * (1 + r)^n / ((1 + r)^n - 1)
     * where P = principal, r = monthly interest rate, n = number of months
     */
    public double calculateEMI(double principal, double annualInterestRate, int tenureInYears) {
        if (principal <= 0 || annualInterestRate < 0 || tenureInYears <= 0) {
            throw new IllegalArgumentException("Invalid EMI calculation parameters");
        }
        
        double monthlyRate = annualInterestRate / 100 / 12;
        int numberOfMonths = tenureInYears * 12;
        
        if (monthlyRate == 0) {
            return principal / numberOfMonths;
        }
        
        double numerator = principal * monthlyRate * Math.pow(1 + monthlyRate, numberOfMonths);
        double denominator = Math.pow(1 + monthlyRate, numberOfMonths) - 1;
        
        return numerator / denominator;
    }
    
    /**
     * Processes the loan application
     */
    public boolean processLoanApplication(int customerId, int age, double monthlySalary,
                                         double existingLoanAmount, int creditScore,
                                         String employmentType, double requestedLoanAmount,
                                         int loanTenure) {
        
        this.customerId = customerId;
        this.age = age;
        this.monthlySalary = monthlySalary;
        this.existingLoanAmount = existingLoanAmount;
        this.creditScore = creditScore;
        this.employmentType = employmentType;
        this.requestedLoanAmount = requestedLoanAmount;
        this.loanTenure = loanTenure;
        this.rejectionReason = "";
        
        try {
            // Validate all inputs
            if (!validateAge(age)) return false;
            if (!validateSalary(monthlySalary)) return false;
            if (!validateCreditScore(creditScore)) return false;
            if (!validateEmploymentType(employmentType)) return false;
            if (!validateExistingLoan(existingLoanAmount, monthlySalary)) return false;
            if (!validateRequestedLoanAmount(requestedLoanAmount)) return false;
            if (!validateTenure(loanTenure)) return false;
            
            // Calculate eligible loan amount
            this.eligibleLoanAmount = calculateEligibleLoanAmount(monthlySalary, existingLoanAmount, 
                                                                  creditScore, employmentType);
            
            // Check if requested loan is within eligible amount
            if (requestedLoanAmount > eligibleLoanAmount) {
                this.rejectionReason = "Requested loan amount exceeds eligible amount of " + 
                                      String.format("%.2f", eligibleLoanAmount);
                return false;
            }
            
            // Determine interest rate
            this.interestRate = determineInterestRate(creditScore);
            
            // Calculate EMI
            this.emi = calculateEMI(requestedLoanAmount, interestRate, loanTenure);
            
            // Calculate debt-to-income ratio
            double totalMonthlyDebt = (existingLoanAmount / 12) + this.emi;
            this.debtToIncomeRatio = calculateDebtToIncomeRatio(totalMonthlyDebt, monthlySalary);
            
            // Final DTI check
            if (this.debtToIncomeRatio > MAX_DTI_RATIO) {
                this.rejectionReason = "Debt-to-income ratio (" + String.format("%.2f", debtToIncomeRatio) + 
                                      ") exceeds maximum allowed (" + String.format("%.2f", MAX_DTI_RATIO) + ")";
                return false;
            }
            
            this.approvalStatus = true;
            return true;
            
        } catch (Exception e) {
            this.rejectionReason = "Exception during processing: " + e.getMessage();
            return false;
        }
    }
    
    // Getters
    public double getDebtToIncomeRatio() { return debtToIncomeRatio; }
    public double getEligibleLoanAmount() { return eligibleLoanAmount; }
    public double getInterestRate() { return interestRate; }
    public double getEMI() { return emi; }
    public boolean isApproved() { return approvalStatus; }
    public String getRejectionReason() { return rejectionReason; }
    
    /**
     * Displays the loan application report
     */
    public void displayReport() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("LOAN APPLICATION REPORT");
        System.out.println("=".repeat(60));
        System.out.println("Customer ID: " + customerId);
        System.out.println("Age: " + age);
        System.out.println("Monthly Salary: ₹" + String.format("%.2f", monthlySalary));
        System.out.println("Existing Loan Amount: ₹" + String.format("%.2f", existingLoanAmount));
        System.out.println("Credit Score: " + creditScore);
        System.out.println("Employment Type: " + employmentType);
        System.out.println("Requested Loan Amount: ₹" + String.format("%.2f", requestedLoanAmount));
        System.out.println("Loan Tenure: " + loanTenure + " years");
        System.out.println("-".repeat(60));
        System.out.println("Eligible Loan Amount: ₹" + String.format("%.2f", eligibleLoanAmount));
        System.out.println("Interest Rate: " + String.format("%.2f", interestRate) + "%");
        System.out.println("EMI: ₹" + String.format("%.2f", emi));
        System.out.println("Debt-to-Income Ratio: " + String.format("%.2f", debtToIncomeRatio * 100) + "%");
        System.out.println("-".repeat(60));
        
        if (approvalStatus) {
            System.out.println("STATUS: ✓ APPROVED");
        } else {
            System.out.println("STATUS: ✗ REJECTED");
            System.out.println("REASON: " + rejectionReason);
        }
        System.out.println("=".repeat(60));
    }
    
    /**
     * Runs the system with a built-in sample application.
     */
    public static void main(String[] args) {
        LoanProcessingSystem lps = new LoanProcessingSystem();

        int customerId = 1001;
        int age = 30;
        double monthlySalary = 75000.0;
        double existingLoanAmount = 120000.0;
        int creditScore = 760;
        String employmentType = "Salaried";
        double requestedLoanAmount = 2000000.0;
        int loanTenure = 15;

        lps.processLoanApplication(customerId, age, monthlySalary,
                                    existingLoanAmount, creditScore,
                                    employmentType, requestedLoanAmount,
                                    loanTenure);
        lps.displayReport();
    }
}
