package se.kth.iv1350.controller;

/**
 * Thrown when an operation in the controller fails due to an underlying
 * system error. This exception wraps lower-level exceptions to present
 * errors at the correct abstraction level for the view.
 */
public class OperationFailedException extends Exception {

    /**
     * Creates a new instance with a message describing the failed operation
     * and the underlying cause.
     *
     * @param message A description of what operation failed.
     * @param cause   The exception that caused the operation to fail.
     */
    public OperationFailedException(String message, Exception cause) {
        super(message, cause);
    }
}
