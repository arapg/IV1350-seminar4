package se.kth.iv1350.view;

import se.kth.iv1350.model.RevenueObserver;
import se.kth.iv1350.util.Amount;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Writes the total revenue from all completed sales to a file.
 * Implements the {@link RevenueObserver} interface to be notified
 * when a sale is completed.
 */
public class TotalRevenueFileOutput implements RevenueObserver {
    private Amount totalRevenue;
    private PrintWriter revenueFile;

    /**
     * Creates a new instance with a total revenue of zero. Opens the
     * file {@code total-revenue.txt} for writing.
     */
    public TotalRevenueFileOutput() {
        this.totalRevenue = new Amount(0, "SEK");
        try {
            revenueFile = new PrintWriter(new FileWriter("total-revenue.txt", true), true);
        } catch (IOException e) {
            System.err.println("Could not open revenue log file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Called when a sale has been completed. Adds the sale total to the
     * accumulated revenue and writes the updated total to a file.
     *
     * @param totalPriceOfSale The total price of the completed sale.
     */
    @Override
    public void newSaleCompleted(Amount totalPriceOfSale) {
        totalRevenue = totalRevenue.add(totalPriceOfSale);
        writeCurrentTotalRevenue();
    }

    private void writeCurrentTotalRevenue() {
        if (revenueFile != null) {
            String timestamp = LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            revenueFile.println(timestamp + " - Total revenue: " + totalRevenue);
        }
    }
}
