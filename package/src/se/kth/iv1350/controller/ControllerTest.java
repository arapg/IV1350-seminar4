package se.kth.iv1350.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import se.kth.iv1350.dto.SaleStateDTO;
import se.kth.iv1350.integration.*;
import se.kth.iv1350.model.Register;
import se.kth.iv1350.util.Amount;

public class ControllerTest {
    private Controller controller;

    @BeforeEach
    public void setUp() {
        InventorySystem invSys = new InventorySystem();
        AccountingSystem accSys = new AccountingSystem();
        DiscountDatabase discountDB = new DiscountDatabase();
        Printer printer = new Printer();
        Register register = new Register();
        controller = new Controller(invSys, accSys, discountDB, printer, register);
        controller.startSale();
    }

    @Test
    public void testEnterValidItemDoesNotThrow() {
        assertDoesNotThrow(() -> controller.enterItem("abc123", 1),
                "Entering a valid item ID should not throw any exception.");
    }

    @Test
    public void testEnterValidItemReturnsSaleState() throws ItemNotFoundException, OperationFailedException {
        SaleStateDTO result = controller.enterItem("abc123", 1);
        assertNotNull(result, "A valid item should return a non-null SaleStateDTO.");
        assertEquals("abc123", result.getLastAddedItem().getItemID(),
                "Returned sale state should contain the added item.");
    }

    @Test
    public void testEnterItemWithInvalidIDThrowsItemNotFoundException() {
        assertThrows(ItemNotFoundException.class,
                () -> controller.enterItem("nonExistentID", 1),
                "Searching for a non-existent item ID should throw ItemNotFoundException.");
    }

    @Test
    public void testItemNotFoundExceptionContainsItemID() {
        try {
            controller.enterItem("badID", 1);
            fail("Expected ItemNotFoundException was not thrown.");
        } catch (ItemNotFoundException e) {
            assertEquals("badID", e.getItemID(),
                    "The exception should contain the item ID that was not found.");
            assertTrue(e.getMessage().contains("badID"),
                    "The exception message should mention the item ID.");
        } catch (OperationFailedException e) {
            fail("Expected ItemNotFoundException but got OperationFailedException.");
        }
    }

    @Test
    public void testDatabaseFailureThrowsOperationFailedException() {
        assertThrows(OperationFailedException.class,
                () -> controller.enterItem("FAIL_DB", 1),
                "A simulated database failure should throw OperationFailedException.");
    }

    @Test
    public void testOperationFailedExceptionHasCause() {
        try {
            controller.enterItem("FAIL_DB", 1);
            fail("Expected OperationFailedException was not thrown.");
        } catch (OperationFailedException e) {
            assertNotNull(e.getCause(),
                    "OperationFailedException should wrap the underlying cause.");
            assertTrue(e.getCause() instanceof DatabaseFailureException,
                    "The cause should be a DatabaseFailureException.");
        } catch (ItemNotFoundException e) {
            fail("Expected OperationFailedException but got ItemNotFoundException.");
        }
    }

    @Test
    public void testSaleStateUnchangedAfterItemNotFoundException()
            throws ItemNotFoundException, OperationFailedException {
        controller.enterItem("abc123", 1);
        Amount totalBefore = controller.endSale();

        controller.startSale();
        controller.enterItem("abc123", 1);
        try {
            controller.enterItem("nonExistentID", 1);
            fail("Expected ItemNotFoundException.");
        } catch (ItemNotFoundException e) {
            // expected
        }
        Amount totalAfter = controller.endSale();

        assertEquals(totalBefore.getValue(), totalAfter.getValue(), 0.01,
                "Sale state should not change when an exception is thrown.");
    }

    @Test
    public void testSaleStateUnchangedAfterDatabaseFailure()
            throws ItemNotFoundException, OperationFailedException {
        controller.enterItem("abc123", 1);
        Amount totalBefore = controller.endSale();

        controller.startSale();
        controller.enterItem("abc123", 1);
        try {
            controller.enterItem("FAIL_DB", 1);
            fail("Expected OperationFailedException.");
        } catch (OperationFailedException e) {
            // expected
        }
        Amount totalAfter = controller.endSale();

        assertEquals(totalBefore.getValue(), totalAfter.getValue(), 0.01,
                "Sale state should not change when a database failure occurs.");
    }
}
