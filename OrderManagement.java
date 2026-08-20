public class OrderManagement {

    static class Product {
        int id, quantity;
        String category;
        double price, discount, tax;

        Product(int id, String category, int quantity, double price,
                double discount, double tax) {
            this.id = id;
            this.category = category;
            this.quantity = quantity;
            this.price = price;
            this.discount = discount;
            this.tax = tax;
        }
    }

    public static void main(String[] args) {

        Product[] products = {
            new Product(101, "Electronics", 2, 20000, 10, 18),
            new Product(102, "Clothing", 3, 1500, 5, 12)
        };

        String coupon = "SAVE10";
        double subtotal = 0;
        double discount = 0;
        double gst = 0;

        for (Product p : products) {

            if (p.quantity <= 0) {
                System.out.println("Invalid quantity for Product " + p.id);
                continue;
            }

            double amount = p.quantity * p.price;
            subtotal += amount;

            double categoryDiscount = amount * p.discount / 100;

            if (p.quantity >= 5) {
                categoryDiscount += amount * 5 / 100;
            }

            discount += categoryDiscount;
            gst += (amount - categoryDiscount) * p.tax / 100;
        }

        double couponDiscount = 0;

        if (coupon.equals("SAVE10")) {
            couponDiscount = subtotal * 10 / 100;
        } else if (!coupon.equals("NONE")) {
            System.out.println("Invalid coupon code");
        }

        double totalDiscount = discount + couponDiscount;

        // Maximum discount allowed is 30% of subtotal
        if (totalDiscount > subtotal * 0.30) {
            totalDiscount = subtotal * 0.30;
        }

        double shipping = (subtotal >= 50000) ? 0 : 500;

        double finalAmount = subtotal - totalDiscount + gst + shipping;

        System.out.println("\n=== ORDER SUMMARY ===");
        System.out.println("Subtotal: ₹" + subtotal);
        System.out.println("Discount: ₹" + totalDiscount);
        System.out.println("GST: ₹" + gst);
        System.out.println("Shipping: ₹" + shipping);
        System.out.println("Final Amount: ₹" + finalAmount);
    }
}