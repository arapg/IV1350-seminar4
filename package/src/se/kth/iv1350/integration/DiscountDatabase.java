package se.kth.iv1350.integration;

/**
 * Handles communication with an external discount database (simulated).
 * Not actively used in the current required flows for Seminar 3.
 */
public class DiscountDatabase {
    /**
     * Creates a new DiscountDatabase instance.
     */
    public DiscountDatabase() {
        // Initialization if needed
    }

    /**
     * Fetches discount information based on a customer ID.
     * (Currently a placeholder as discounts are not part of the required flow).
     * @param customerID The ID of the customer.
     * @return Discount information (structure TBD if implemented).
     */
    public Object fetchDiscount(String customerID) {
        System.out.println("LOG: DiscountDatabase queried for customer ID: " + customerID);
        return null;
    }
}