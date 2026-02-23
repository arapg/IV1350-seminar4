package se.kth.iv1350;

import se.kth.iv1350.controller.Controller;
import se.kth.iv1350.integration.*;
import se.kth.iv1350.model.Register;
import se.kth.iv1350.view.View;

/**
 * Starts the entire application, contains the main method used to start the application.
 */
public class main {
    /**
     * The main method used to start the application.
     * @param args The application does not take any command line parameters.
     */
    public static void main(String[] args) {
        // Create integration layer objects
        InventorySystem invSys = new InventorySystem();
        AccountingSystem accSys = new AccountingSystem();
        DiscountDatabase discountDB = new DiscountDatabase();
        Printer printer = new Printer();
        
        // Create model layer objects that are managed by Controller/Main
        Register register = new Register();

        // Create controller
        Controller contr = new Controller(invSys, accSys, discountDB, printer, register);

        // Create view
        View view = new View(contr);
        
        // Start the application by running the interactive sale
        view.startSale(); 
    }
}
