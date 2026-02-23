package se.kth.iv1350.dto;
import se.kth.iv1350.util.Amount;

/**
 * Represents the current state of a sale, typically returned after adding an item.
 * This is a Data Transfer Object.
 */
public class SaleStateDTO {
    private final ItemDTO lastAddedItem;
    private final Amount runningTotalIncludingVAT;
    private final Amount currentTotalVAT;

    /**
     * Creates a new instance representing the state of the sale.
     * @param lastAddedItem The most recently added item's details.
     * @param runningTotalIncludingVAT The current running total of the sale, including VAT.
     * @param currentTotalVAT The current total VAT amount for the sale.
     */
    public SaleStateDTO(ItemDTO lastAddedItem, Amount runningTotalIncludingVAT, Amount currentTotalVAT) {
        this.lastAddedItem = lastAddedItem;
        this.runningTotalIncludingVAT = runningTotalIncludingVAT;
        this.currentTotalVAT = currentTotalVAT;
    }

    /**
     * Gets the details of the last item added to the sale.
     * @return The ItemDTO of the last added item.
     */
    public ItemDTO getLastAddedItem() {
        return lastAddedItem;
    }

    /**
     * Gets the current running total of the sale, including VAT.
     * @return The running total as an Amount.
     */
    public Amount getRunningTotalIncludingVAT() {
        return runningTotalIncludingVAT;
    }

    /**
     * Gets the current total VAT accumulated for the sale.
     * @return The total VAT as an Amount.
     */
    public Amount getCurrentTotalVAT() {
        return currentTotalVAT;
    }
}
