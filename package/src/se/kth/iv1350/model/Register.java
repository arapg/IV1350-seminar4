package se.kth.iv1350.model;

import se.kth.iv1350.util.Amount;

/**
 * Represents the cash register in the store.
 */
public class Register {
    private Amount balance;

    /**
     * Creates a new Register instance with an initial balance of zero.
     */
    public Register() {
        this.balance = new Amount(0, "SEK"); // Assuming SEK, can be configured
    }

    /**
     * Updates the register's balance after a sale.
     * The amount added to the balance is the amount paid by the customer for the sale.
     * @param amountPaidToRegister The amount effectively added to the register (total sale price).
     */
    public void updateRegister(Amount amountPaidToRegister) {
        if (amountPaidToRegister != null) {
            this.balance = this.balance.add(amountPaidToRegister);
        }
    }

    /**
     * Gets the current balance of the register.
     * @return The current balance.
     */
    public Amount getBalance() {
        return balance;
    }
}