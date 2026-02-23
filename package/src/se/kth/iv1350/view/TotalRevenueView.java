package se.kth.iv1350.view;

import se.kth.iv1350.model.RevenueObserver;
import se.kth.iv1350.util.Amount;

/**
 * Shows the total revenue from all completed sales on the user interface.
 * Implements the {@link RevenueObserver} interface to be notified
 * when a sale is completed.
 */
public class TotalRevenueView implements RevenueObserver {
    private Amount totalRevenue;

    /**
     * Creates a new instance with a total revenue of zero.
     */
    public TotalRevenueView() {
        this.totalRevenue = new Amount(0, "SEK");
    }

    /**
     * Called when a sale has been completed. Adds the sale total to the
     * accumulated revenue and displays the updated total.
     *
     * @param totalPriceOfSale The total price of the completed sale.
     */
    @Override
    public void newSaleCompleted(Amount totalPriceOfSale) {
        totalRevenue = totalRevenue.add(totalPriceOfSale);
        printCurrentTotalRevenue();
    }

    private void printCurrentTotalRevenue() {
        System.out.println();
        System.out.println("### Total Revenue ###");
        System.out.println("Total income since program start: " + totalRevenue);
        System.out.println("#####################");
        System.out.println();
    }
}
