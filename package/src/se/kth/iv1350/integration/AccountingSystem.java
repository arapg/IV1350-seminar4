package se.kth.iv1350.integration;

import se.kth.iv1350.model.Sale;

/**
 * Handles communication with an external accounting system (simulated).
 */
public class AccountingSystem {
    /**
     * Creates a new AccountingSystem instance.
     */
    public AccountingSystem() {
    }

    /**
     * Registers a completed sale in the accounting system.
     * (Currently a placeholder).
     * @param currentSale The sale to be registered.
     */
    public void registerSale(Sale currentSale) {
        // In a real system, this would send sale data to an accounting service
        System.out.println("LOG: Accounting system notified. Sale total: " + currentSale.getRunningTotalIncludingVAT());
    }
}