package se.kth.iv1350.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import se.kth.iv1350.dto.ItemDTO;
import se.kth.iv1350.dto.SaleStateDTO;
import se.kth.iv1350.util.Amount;

import java.util.ArrayList;
import java.util.List;

public class SaleTest {
    private Sale sale;
    private ItemDTO oatmeal;
    private ItemDTO yoghurt;

    @BeforeEach
    public void setUp() {
        sale = new Sale();
        oatmeal = new ItemDTO("abc123", "BigWheel Oatmeal",
                "BigWheel Oatmeal 500g", new Amount(29.90, "SEK"), 0.06);
        yoghurt = new ItemDTO("def456", "YouGoGo Blueberry",
                "YouGoGo Blueberry 240g", new Amount(14.90, "SEK"), 0.06);
    }

    @Test
    public void testAddItemUpdatesRunningTotal() {
        sale.addItemToSale(oatmeal, 1);
        double expectedTotal = 29.90 + 29.90 * 0.06;
        assertEquals(expectedTotal, sale.getRunningTotalIncludingVAT().getValue(), 0.01,
                "Running total should include item price plus VAT.");
    }

    @Test
    public void testAddDuplicateItemIncreasesQuantityInsteadOfAddingNewLine() {
        sale.addItemToSale(oatmeal, 1);
        sale.addItemToSale(oatmeal, 2);

        assertEquals(1, sale.getItems().size(),
                "Adding the same item twice should not create a second line item.");
        assertEquals(3, sale.getItems().get(0).getQuantity(),
                "Quantity should be the sum of both additions.");
    }

    @Test
    public void testRunningTotalWithMultipleDifferentItems() {
        sale.addItemToSale(oatmeal, 3);
        sale.addItemToSale(yoghurt, 2);

        double expectedOatmealTotal = 3 * (29.90 + 29.90 * 0.06);
        double expectedYoghurtTotal = 2 * (14.90 + 14.90 * 0.06);
        double expectedTotal = expectedOatmealTotal + expectedYoghurtTotal;

        assertEquals(expectedTotal, sale.getRunningTotalIncludingVAT().getValue(), 0.01,
                "Running total should be the combined price+VAT of all items.");
    }

    @Test
    public void testTotalVATIsCalculatedCorrectly() {
        sale.addItemToSale(oatmeal, 2);
        double expectedVAT = 2 * 29.90 * 0.06;
        assertEquals(expectedVAT, sale.getCurrentTotalVAT().getValue(), 0.01,
                "Total VAT should be price * vatRate * quantity for all items.");
    }

    @Test
    public void testMakePaymentReturnsCorrectChange() {
        sale.addItemToSale(oatmeal, 1);
        Amount totalIncVAT = sale.getRunningTotalIncludingVAT();
        Amount payment = new Amount(100, "SEK");

        Amount change = sale.makePayment(payment);

        assertNotNull(change, "Change should not be null for sufficient payment.");
        double expectedChange = 100 - totalIncVAT.getValue();
        assertEquals(expectedChange, change.getValue(), 0.01,
                "Change should be payment minus total including VAT.");
    }

    @Test
    public void testMakePaymentWithInsufficientAmountReturnsNull() {
        sale.addItemToSale(oatmeal, 1);
        Amount insufficientPayment = new Amount(1.00, "SEK");

        Amount change = sale.makePayment(insufficientPayment);

        assertNull(change, "Insufficient payment should return null.");
    }

    @Test
    public void testAddNullItemReturnsNull() {
        SaleStateDTO result = sale.addItemToSale(null, 1);
        assertNull(result, "Adding a null item should return null.");
    }

    @Test
    public void testMakePaymentWithExactAmountReturnsZeroChange() {
        sale.addItemToSale(yoghurt, 1);
        Amount exactPayment = new Amount(sale.getRunningTotalIncludingVAT().getValue(), "SEK");

        Amount change = sale.makePayment(exactPayment);

        assertNotNull(change, "Exact payment should not return null.");
        assertEquals(0, change.getValue(), 0.01,
                "Paying the exact amount should give zero change.");
    }

    @Test
    public void testRevenueObserverIsNotifiedOnPayment() {
        final boolean[] wasNotified = {false};

        List<RevenueObserver> observers = new ArrayList<>();
        observers.add(totalPriceOfSale -> wasNotified[0] = true);
        sale.addRevenueObservers(observers);

        sale.addItemToSale(oatmeal, 1);
        sale.makePayment(new Amount(100, "SEK"));

        assertTrue(wasNotified[0], "Observer should be notified when payment is made.");
    }

    @Test
    public void testRevenueObserverReceivesCorrectAmount() {
        final double[] receivedAmount = {0};

        List<RevenueObserver> observers = new ArrayList<>();
        observers.add(totalPriceOfSale -> receivedAmount[0] = totalPriceOfSale.getValue());
        sale.addRevenueObservers(observers);

        sale.addItemToSale(oatmeal, 1);
        sale.makePayment(new Amount(100, "SEK"));

        double expectedTotal = 29.90 + 29.90 * 0.06;
        assertEquals(expectedTotal, receivedAmount[0], 0.01,
                "Observer should receive the total price including VAT.");
    }
}
