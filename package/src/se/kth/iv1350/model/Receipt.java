package se.kth.iv1350.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import se.kth.iv1350.util.Amount;

/**
 * Represents a receipt for a completed sale.
 */
public class Receipt {
    private LocalDateTime saleTimestamp;
    private List<SalesLineItem> items;
    private Amount totalPriceIncludingVAT;
    private Amount totalVAT;
    private Amount amountPaid;
    private Amount change;

    /**
     * Creates a new, empty Receipt instance. Data is populated later.
     */
    public Receipt() {
        // Data will be populated by the Sale or Controller
    }

    /**
     * Populates the receipt with details from a completed sale.
     * This method is called when the sale is finalized and payment is made.
     * @param sale The sale for which this receipt is generated.
     * @param amountPaid The amount paid by the customer.
     * @param change The change given back to the customer.
     */
    public void populateReceipt(Sale sale, Amount amountPaid, Amount change) {
        this.saleTimestamp = sale.getSaleTimestamp();
        this.items = sale.getItems(); // Get a copy if Sale.items can be modified later
        this.totalPriceIncludingVAT = sale.getRunningTotalIncludingVAT(); // Or a final total if different
        this.totalVAT = sale.getCurrentTotalVAT();
        this.amountPaid = amountPaid;
        this.change = change;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        sb.append("---------------- Begin receipt ----------------\n");
        sb.append("Time of Sale: ").append(saleTimestamp.format(formatter)).append("\n\n");

        for (SalesLineItem lineItem : items) {
            sb.append(String.format("%-25s %d x %s\t%s\n",
                    lineItem.getItem().getName(),
                    lineItem.getQuantity(),
                    lineItem.getItem().getPrice().toString().replace(" SEK", ""), 
                    lineItem.getTotalPrice()));
        }
        sb.append("\n");
        sb.append("Total: \t\t\t\t\t").append(totalPriceIncludingVAT).append("\n");
        sb.append("VAT:   \t\t\t\t\t").append(totalVAT).append("\n\n");
        sb.append("Cash:  \t\t\t\t\t").append(amountPaid).append("\n");
        sb.append("Change:\t\t\t\t\t").append(change).append("\n");
        sb.append("----------------- End receipt -----------------\n");

        return sb.toString();
    }
}