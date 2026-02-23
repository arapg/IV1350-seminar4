package se.kth.iv1350.model;

import se.kth.iv1350.util.Amount;

/**
 * A listener interface for receiving notifications when a sale has been
 * completed and payment has been made. Classes that are interested in
 * tracking total revenue should implement this interface.
 */
public interface RevenueObserver {

    /**
     * Invoked when a sale has been completed and paid for.
     *
     * @param totalPriceOfSale The total price (including VAT) of the completed sale.
     */
    void newSaleCompleted(Amount totalPriceOfSale);
}
