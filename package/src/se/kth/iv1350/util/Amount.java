package se.kth.iv1350.util;

import java.util.Objects;

/**
 * Represents an amount of money with a currency.
 */
public class Amount {
    private final double value;
    private final String currency;

    /**
     * Creates a new instance representing an amount of money.
     * @param value The numerical value of the amount.
     * @param currency The currency of the amount (e.g., "SEK").
     */
    public Amount(double value, String currency) {
        this.value = value;
        this.currency = currency;
    }

    /**
     * Gets the numerical value of the amount.
     * @return The value.
     */
    public double getValue() {
        return value;
    }

    /**
     * Gets the currency of the amount.
     * @return The currency string.
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * Adds another amount to this amount.
     * @param other The amount to add.
     * @return A new Amount object representing the sum.
     *         Returns null if currencies do not match.
     */
    public Amount add(Amount other) {
        if (other == null || !this.currency.equals(other.currency)) {
            return null; // Or handle currency mismatch differently if allowed
        }
        return new Amount(this.value + other.value, this.currency);
    }

    /**
     * Subtracts another amount from this amount.
     * @param other The amount to subtract.
     * @return A new Amount object representing the difference.
     *         Returns null if currencies do not match.
     */
    public Amount subtract(Amount other) {
        if (other == null || !this.currency.equals(other.currency)) {
            return null;
        }
        return new Amount(this.value - other.value, this.currency);
    }

    /**
     * Multiplies this amount by a factor.
     * @param factor The factor to multiply by.
     * @return A new Amount object representing the product.
     */
    public Amount multiply(double factor) {
        return new Amount(this.value * factor, this.currency);
    }
    
    /**
     * Multiplies this amount by an integer quantity.
     * @param quantity The quantity to multiply by.
     * @return A new Amount object representing the product.
     */
    public Amount multiply(int quantity) {
        return new Amount(this.value * quantity, this.currency);
    }


    @Override
    public String toString() {
        return String.format("%.2f %s", value, currency).replace('.', ':'); // Match sample output format
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount = (Amount) o;
        return Double.compare(amount.value, value) == 0 &&
               Objects.equals(currency, amount.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, currency);
    }
}