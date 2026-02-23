package se.kth.iv1350.model;

import se.kth.iv1350.dto.ItemDTO;
import se.kth.iv1350.dto.SaleStateDTO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import se.kth.iv1350.util.Amount;

/**
 * Represents a single sale transaction.
 */
public class Sale {
    private LocalDateTime saleTimestamp;
    private List<SalesLineItem> items;
    private Amount runningTotalIncludingVAT;
    private Amount currentTotalVAT;
    private Receipt receipt;
    private List<RevenueObserver> revenueObservers = new ArrayList<>();

    /**
     * Creates a new Sale instance.
     * Initializes the sale time, item list, and totals.
     */
    public Sale() {
        this.saleTimestamp = LocalDateTime.now();
        this.items = new ArrayList<>();
        this.runningTotalIncludingVAT = new Amount(0, "SEK");
        this.currentTotalVAT = new Amount(0, "SEK");
        this.receipt = new Receipt();
    }

    /**
     * Sets the time of the sale. Typically called when the sale is initiated.
     */
    public void setTimeOfSale() {
        this.saleTimestamp = LocalDateTime.now();
    }

    /**
     * Gets the timestamp of when the sale was initiated.
     * @return The sale timestamp.
     */
    public LocalDateTime getSaleTimestamp() {
        return saleTimestamp;
    }

    /**
     * Gets the list of items currently in the sale.
     * @return A list of {@link SalesLineItem}s.
     */
    public List<SalesLineItem> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Gets the current running total of the sale, including VAT.
     * @return The running total.
     */
    public Amount getRunningTotalIncludingVAT() {
        return runningTotalIncludingVAT;
    }

    /**
     * Gets the current total VAT accumulated for the sale.
     * @return The total VAT.
     */
    public Amount getCurrentTotalVAT() {
        return currentTotalVAT;
    }

    /**
     * Registers observers that will be notified when this sale is paid for.
     *
     * @param observers The observers to register.
     */
    public void addRevenueObservers(List<RevenueObserver> observers) {
        revenueObservers.addAll(observers);
    }

    /**
     * Adds an item to the sale or increases its quantity if already present.
     * Updates the running total and VAT.
     * @param itemInfo The DTO of the item to add.
     * @param quantity The quantity of the item to add.
     * @return A {@link SaleStateDTO} representing the current state of the sale after adding the item.
     *         Returns null if itemInfo is null.
     */
    public SaleStateDTO addItemToSale(ItemDTO itemInfo, int quantity) {
        if (itemInfo == null) {
            return null;
        }

        SalesLineItem existingItem = findItem(itemInfo.getItemID());
        if (existingItem != null) {
            existingItem.increaseQuantity(quantity);
        } else {
            SalesLineItem newItemLine = new SalesLineItem(itemInfo, quantity);
            items.add(newItemLine);
        }

        calculateRunningTotalAndVAT();

        return new SaleStateDTO(itemInfo, runningTotalIncludingVAT, currentTotalVAT);
    }

    private SalesLineItem findItem(String itemID) {
        for (SalesLineItem lineItem : items) {
            if (lineItem.getItem().getItemID().equals(itemID)) {
                return lineItem;
            }
        }
        return null;
    }

    private void calculateRunningTotalAndVAT() {
        Amount newRunningTotal = new Amount(0, "SEK");
        Amount newTotalVAT = new Amount(0, "SEK");

        for (SalesLineItem lineItem : items) {
            Amount itemPrice = lineItem.getItem().getPrice();
            double vatRate = lineItem.getItem().getVatRate();
            int quantity = lineItem.getQuantity();

            Amount itemSubtotal = itemPrice.multiply(quantity);
            Amount itemVAT = itemPrice.multiply(vatRate).multiply(quantity);

            newRunningTotal = newRunningTotal.add(itemSubtotal.add(itemVAT));
            newTotalVAT = newTotalVAT.add(itemVAT);
        }
        this.runningTotalIncludingVAT = newRunningTotal;
        this.currentTotalVAT = newTotalVAT;
    }

    /**
     * Calculates the final total price of the sale (including VAT).
     * @return The total price.
     */
    public Amount calculateTotal() {
        return runningTotalIncludingVAT;
    }

    /**
     * Processes the payment for the sale. Notifies revenue observers
     * on successful payment.
     *
     * @param amountTendered The amount of money tendered by the customer.
     * @return The change to be given back to the customer. Returns null if payment is insufficient.
     */
    public Amount makePayment(Amount amountTendered) {
        if (amountTendered == null || amountTendered.getValue() < runningTotalIncludingVAT.getValue()) {
            return null;
        }
        Amount change = amountTendered.subtract(runningTotalIncludingVAT);
        this.receipt.populateReceipt(this, amountTendered, change);
        notifyObservers();
        return change;
    }

    private void notifyObservers() {
        for (RevenueObserver observer : revenueObservers) {
            observer.newSaleCompleted(runningTotalIncludingVAT);
        }
    }

    /**
     * Gets the receipt associated with this sale.
     * The receipt should be populated after payment.
     * @return The {@link Receipt} object.
     */
    public Receipt getReceipt() {
        return receipt;
    }
}
