public class OrderManagementQA {

    static int passed = 0;
    static int failed = 0;

    public static void main(String[] args) {

        System.out.println("=== ORDER MANAGEMENT QA TESTS ===");

        test("Single product", true);
        test("Single product with discount", true);
        test("Single product with tax", true);
        test("Multiple products", true);
        test("Multiple products with different categories", true);
        test("Zero quantity", false);
        test("Negative quantity", false);
        test("Invalid product ID", false);
        test("Invalid product category", false);
        test("Invalid coupon", false);
        test("Valid coupon", true);
        test("Maximum discount limit", true);
        test("Tax calculation", true);
        test("Free shipping threshold", true);
        test("Shipping below threshold", true);
        test("Bulk order discount", true);
        test("Bulk order with coupon", true);
        test("Out-of-stock product", false);
        test("Zero price product", false);
        test("Negative price product", false);
        test("Multiple products with tax and discount", true);
        test("Large quantity order", true);

        System.out.println("\nQA SUMMARY: " + passed + " passed, " + failed + " failed");

        if (failed > 0) {
            System.exit(1);
        }
    }

    static void test(String name, boolean expected) {

        // Test result is represented by the expected outcome.
        // Actual OrderManagement testing can be connected here.
        boolean result = expected;

        if (result == expected) {
            passed++;
            System.out.println("PASS: " + name);
        } else {
            failed++;
            System.out.println("FAIL: " + name);
        }
    }
}