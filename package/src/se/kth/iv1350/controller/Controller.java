package se.kth.iv1350.controller;

import se.kth.iv1350.util.Amount;
import se.kth.iv1350.dto.*;
import se.kth.iv1350.integration.*;
import se.kth.iv1350.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The application controller that handles all calls from the view to the model and integration layers.
 */
public class Controller {
    private InventorySystem invSys;
    private AccountingSystem accSys;
    private DiscountDatabase discountDB;
    private Printer printer;
    private Register register;
    private Sale currentSale;
    private List<RevenueObserver> revenueObservers = new ArrayList<>();

    /**
     * Creates a new Controller instance.
     * @param invSys The inventory system handler.
     * @param accSys The accounting system handler.
     * @param discountDB The discount database handler.
     * @param printer The printer handler.
     * @param register The cash register handler.
     */
    public Controller(InventorySystem invSys, AccountingSystem accSys, DiscountDatabase discountDB, Printer printer, Register register) {
        this.invSys = invSys;
        this.accSys = accSys;
        this.discountDB = discountDB;
        this.printer = printer;
        this.register = register;
    }

    /**
     * Adds an observer that will be notified whenever a sale has been completed.
     *
     * @param observer The observer to notify.
     */
    public void addRevenueObserver(RevenueObserver observer) {
        revenueObservers.add(observer);
    }

    /**
     * Starts a new sale. This must be called before any items can be added.
     */
    public void startSale() {
        this.currentSale = new Sale();
        currentSale.addRevenueObservers(revenueObservers);
    }

    /**
     * Enters an item into the current sale.
     *
     * @param itemID   The ID of the item to enter.
     * @param quantity The quantity of the item.
     * @return A {@link SaleStateDTO} representing the current state of the sale.
     * @throws ItemNotFoundException      If the specified item ID does not exist in the inventory.
     * @throws OperationFailedException   If the item could not be retrieved due to a system error.
     */
    public SaleStateDTO enterItem(String itemID, int quantity)
            throws ItemNotFoundException, OperationFailedException {
        if (currentSale == null) {
            return null;
        }
        try {
            ItemDTO itemInfo = invSys.retrieveItemInfo(itemID);
            return currentSale.addItemToSale(itemInfo, quantity);
        } catch (ItemNotFoundException e) {
            throw e;
        } catch (DatabaseFailureException e) {
            throw new OperationFailedException(
                    "Could not retrieve item information for item ID: " + itemID, e);
        }
    }

    /**
     * Ends the current sale and returns the total price.
     * @return The total price (including VAT) of the sale as an {@link Amount}.
     *         Returns null if no sale is active.
     */
    public Amount endSale() {
        if (currentSale == null) {
            return null;
        }
        return currentSale.calculateTotal();
    }

    /**
     * Processes the payment for the current sale.
     * Updates external systems, prints a receipt, and updates the register.
     * @param amountTendered The amount paid by the customer.
     * @return The change to be given to the customer as an {@link Amount}.
     *         Returns null if no sale is active or payment is insufficient.
     */
    public Amount enterPayment(Amount amountTendered) {
        if (currentSale == null) {
            return null;
        }
        Amount change = currentSale.makePayment(amountTendered);
        if (change == null) {
            return null;
        }

        accSys.registerSale(currentSale);
        invSys.updateInventory(currentSale);

        Amount amountPaidForSale = currentSale.getRunningTotalIncludingVAT();
        register.updateRegister(amountPaidForSale);

        Receipt receipt = currentSale.getReceipt();
        printer.printReceipt(receipt);

        return change;
    }

    /**
     * Signals a discount request for the current customer.
     * (Not implemented for Seminar 3 required flow).
     * @param customerID The ID of the customer requesting a discount.
     * @return The total price after discount, or null.
     */
    public Amount signalDiscountCustomer(String customerID) {
        if (currentSale == null) {
            return null;
        }
        System.out.println("LOG: signalDiscountCustomer called. (Not implemented for Seminar 3 scope)");
        return currentSale.calculateTotal();
    }
}
