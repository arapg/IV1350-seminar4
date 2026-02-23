package se.kth.iv1350.dto;
import se.kth.iv1350.util.Amount;

/**
 * Represents an item's data. This is a Data Transfer Object.
 */
public class ItemDTO {
    private final String itemID;
    private final String name;
    private final String description;
    private final Amount price;
    private final double vatRate; // e.g., 0.06 for 6%

    /**
     * Creates a new instance of an ItemDTO.
     * @param itemID The unique identifier for the item.
     * @param name The name of the item.
     * @param description A description of the item.
     * @param price The price of the item.
     * @param vatRate The VAT rate applicable to this item (e.g., 0.06 for 6%).
     */
    public ItemDTO(String itemID, String name, String description, Amount price, double vatRate) {
        this.itemID = itemID;
        this.name = name;
        this.description = description;
        this.price = price;
        this.vatRate = vatRate;
    }

    /**
     * Gets the item ID.
     * @return The item ID.
     */
    public String getItemID() {
        return itemID;
    }

    /**
     * Gets the item name.
     * @return The item name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the item description.
     * @return The item description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the item price.
     * @return The item price.
     */
    public Amount getPrice() {
        return price;
    }

    /**
     * Gets the VAT rate for the item.
     * @return The VAT rate (e.g., 0.06 for 6%).
     */
    public double getVatRate() {
        return vatRate;
    }

    @Override
    public String toString() {
        return "Item ID: " + itemID + "\n" +
               "Item name: " + name + "\n" +
               "Item cost: " + price + "\n" +
               "VAT: " + (int)(vatRate * 100) + "%\n" +
               "Item description: " + description;
    }
}
