package se.kth.iv1350.model;

import se.kth.iv1350.util.Amount;
import se.kth.iv1350.dto.ItemDTO;

/**
 * Represents a line item in a sale, consisting of an item and its quantity.
 */
public class SalesLineItem {
    private ItemDTO item;
    private int quantity;

    /**
     * Creates a new SalesLineItem.
     * @param item The DTO of the item being sold.
     * @param quantity The quantity of this item.
     */
    public SalesLineItem(ItemDTO item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    /**
     * Gets the item DTO.
     * @return The item DTO.
     */
    public ItemDTO getItem() {
        return item;
    }

    /**
     * Gets the quantity of this item.
     * @return The quantity.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Increases the quantity of this item.
     * @param additionalQuantity The quantity to add.
     */
    public void increaseQuantity(int additionalQuantity) {
        this.quantity += additionalQuantity;
    }
    
    /**
     * Calculates the total price for this line item (price * quantity).
     * @return The total price for this line item.
     */
    public Amount getTotalPrice() {
        return item.getPrice().multiply(quantity);
    }

    /**
     * Calculates the total VAT for this line item.
     * @return The total VAT for this line item.
     */
    public Amount getTotalVAT() {
        Amount itemPrice = item.getPrice();
        double vatRate = item.getVatRate();
        return itemPrice.multiply(vatRate).multiply(quantity);
    }
}
