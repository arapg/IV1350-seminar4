package se.kth.iv1350.integration;

import se.kth.iv1350.util.Amount;
import se.kth.iv1350.dto.ItemDTO;
import se.kth.iv1350.model.Sale;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles communication with an external inventory system (simulated).
 */
public class InventorySystem {
    private static final String DATABASE_FAILURE_ITEM_ID = "FAIL_DB";
    private Map<String, ItemDTO> inventory = new HashMap<>();

    /**
     * Creates a new InventorySystem and populates it with some sample items.
     */
    public InventorySystem() {
        inventory.put("abc123", new ItemDTO("abc123", "BigWheel Oatmeal",
                "BigWheel Oatmeal 500g, whole grain oats, high fiber, gluten free",
                new Amount(29.90, "SEK"), 0.06));
        inventory.put("def456", new ItemDTO("def456", "YouGoGo Blueberry",
                "YouGoGo Blueberry 240g, low sugar youghurt, blueberry flavour",
                new Amount(14.90, "SEK"), 0.06));
        inventory.put("ghi789", new ItemDTO("ghi789", "Luxury Chocolate",
                "Dark chocolate 70%",
                new Amount(50.00, "SEK"), 0.12));
    }

    /**
     * Retrieves item information based on its ID.
     *
     * @param itemID The ID of the item to retrieve.
     * @return The {@link ItemDTO} if found.
     * @throws ItemNotFoundException      If the specified item ID does not exist in the inventory.
     * @throws DatabaseFailureException   If the database cannot be reached. Simulated by using
     *                                    the item ID {@value #DATABASE_FAILURE_ITEM_ID}.
     */
    public ItemDTO retrieveItemInfo(String itemID) throws ItemNotFoundException {
        if (DATABASE_FAILURE_ITEM_ID.equals(itemID)) {
            throw new DatabaseFailureException();
        }

        ItemDTO found = inventory.get(itemID);
        if (found == null) {
            throw new ItemNotFoundException(itemID);
        }
        return found;
    }

    /**
     * Updates the inventory system after a sale is completed.
     * (Currently a placeholder).
     *
     * @param sale The completed sale information.
     */
    public void updateInventory(Sale sale) {
        System.out.println("LOG: Inventory system notified of sale. Items sold:");
        sale.getItems().forEach(itemLine -> System.out
                .println("LOG: - " + itemLine.getItem().getName() + ", Qty: " + itemLine.getQuantity()));
    }
}
