package se.kth.iv1350.integration;

import se.kth.iv1350.model.Receipt;

/**
 * Handles printing operations (simulated by printing to System.out).
 */
public class Printer {
    /**
     * Creates a new Printer instance.
     */
    public Printer() {
    }

    /**
     * Prints the specified receipt.
     * In this simulation, it prints to the console.
     * @param receipt The receipt to be printed.
     */
    public void printReceipt(Receipt receipt) {
        if (receipt != null) {
            System.out.println(receipt.toString());
        } else {
            System.out.println("ERROR: Receipt object was null, cannot print.");
        }
    }
}
