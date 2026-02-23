package se.kth.iv1350.integration;

/**
 * Thrown when an item cannot be found in the inventory system.
 * This is a checked exception since item-not-found is a recoverable
 * situation where the user can retry with a different item identifier.
 */
public class ItemNotFoundException extends Exception {
    private final String itemID;

    /**
     * Creates a new instance indicating which item could not be found.
     *
     * @param itemID The item identifier that was not found.
     */
    public ItemNotFoundException(String itemID) {
        super("No item with ID \"" + itemID + "\" exists in the inventory.");
        this.itemID = itemID;
    }

    /**
     * Gets the item identifier that was not found.
     *
     * @return The item identifier that caused this exception.
     */
    public String getItemID() {
        return itemID;
    }
}
