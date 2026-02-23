package se.kth.iv1350.view;

import se.kth.iv1350.controller.Controller;
import se.kth.iv1350.controller.OperationFailedException;
import se.kth.iv1350.dto.*;
import se.kth.iv1350.integration.ItemNotFoundException;
import se.kth.iv1350.util.Amount;
import se.kth.iv1350.util.FileLogger;

import java.util.Scanner;

/**
 * Represents the user interface of the application.
 */
public class View {
    private final Controller contr;
    private final Scanner inputScanner;
    private final FileLogger logger;

    /**
     * Creates a new View instance.
     *
     * @param contr The controller to use for all operations.
     */
    public View(Controller contr) {
        this.contr = contr;
        this.inputScanner = new Scanner(System.in);
        this.logger = new FileLogger();

        contr.addRevenueObserver(new TotalRevenueView());
        contr.addRevenueObserver(new TotalRevenueFileOutput());
    }

    /**
     * Starts the sale, allowing the user to input item IDs and quantities.
     */
    public void startSale() {
        contr.startSale();
        System.out.println("New sale started.");
        System.out.println("------------------------------------");

        while (true) {
            System.out.print("Enter item ID (or 'done' to finish adding items): ");
            String itemID = inputScanner.nextLine().trim();

            if (itemID.equalsIgnoreCase("done")) {
                break;
            }

            int quantity = 0;
            boolean validQuantity = false;
            while (!validQuantity) {
                System.out.print("Enter quantity for item '" + itemID + "': ");
                String quantityStr = inputScanner.nextLine().trim();
                try {
                    quantity = Integer.parseInt(quantityStr);
                    if (quantity > 0) {
                        validQuantity = true;
                    } else {
                        System.out.println("Quantity must be a positive integer. Please try again.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity format. Please enter a number.");
                }
            }

            try {
                SaleStateDTO saleState = contr.enterItem(itemID, quantity);
                if (saleState != null) {
                    System.out.println("--- Item Added/Updated ---");
                    printSaleState(saleState);
                }
            } catch (ItemNotFoundException e) {
                System.out.println("Item not found: " + e.getMessage());
                System.out.println();
            } catch (OperationFailedException e) {
                System.out.println("An error occurred, please try again. "
                        + "If the problem persists, contact support.");
                System.out.println();
                logger.logException(e);
            }
        }

        System.out.println("------------------------------------");
        System.out.println("Ending sale...");
        Amount totalPrice = contr.endSale();
        if (totalPrice != null) {
            System.out.println("Total cost (incl VAT): " + totalPrice);
        } else {
            System.out.println("Error: No sale active or could not calculate total.");
            inputScanner.close();
            return;
        }

        System.out.println("------------------------------------");
        double amountPaidValue = 0;
        boolean validPayment = false;
        while (!validPayment) {
            System.out.print("Enter amount paid by customer: ");
            String amountPaidStr = inputScanner.nextLine().trim();
            try {
                amountPaidValue = Double.parseDouble(amountPaidStr);
                if (amountPaidValue >= 0) {
                    validPayment = true;
                } else {
                    System.out.println("Payment amount cannot be negative.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount format. Please enter a number (e.g., 100 or 75.50).");
            }
        }

        Amount amountPaid = new Amount(amountPaidValue, "SEK");
        System.out.println("Customer pays: " + amountPaid);

        Amount change = contr.enterPayment(amountPaid);

        if (change != null) {
            System.out.println("Change to give the customer: " + change);
        } else {
            System.out.println("Payment failed: Insufficient amount or other error.");
        }
        System.out.println("------------------------------------");
        System.out.println("Sale complete.");
        inputScanner.close();
    }

    private void printSaleState(SaleStateDTO saleState) {
        ItemDTO item = saleState.getLastAddedItem();
        System.out.println("Last Item: " + item.getName() + " (ID: " + item.getItemID() + ")");
        System.out.println("  Cost: " + item.getPrice());
        System.out.println("  VAT: " + (int) (item.getVatRate() * 100) + "%");
        System.out.println("  Description: " + item.getDescription());
        System.out.println("Running Total (incl VAT): " + saleState.getRunningTotalIncludingVAT());
        System.out.println("Current Total VAT: " + saleState.getCurrentTotalVAT());
        System.out.println();
    }
}
