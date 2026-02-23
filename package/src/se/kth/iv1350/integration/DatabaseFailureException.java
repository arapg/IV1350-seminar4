package se.kth.iv1350.integration;

/**
 * Thrown when a call to the inventory database fails.
 * This is an unchecked exception since a database failure is an unexpected
 * system-level error that cannot be handled by the caller in a meaningful way.
 */
public class DatabaseFailureException extends RuntimeException {

    /**
     * Creates a new instance with a message describing the database failure.
     */
    public DatabaseFailureException() {
        super("Could not connect to the inventory database.");
    }

    /**
     * Creates a new instance with a message and the underlying cause.
     *
     * @param cause The exception that caused the database failure.
     */
    public DatabaseFailureException(Exception cause) {
        super("Could not connect to the inventory database.", cause);
    }
}
