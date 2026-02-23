package se.kth.iv1350.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import se.kth.iv1350.dto.ItemDTO;
import se.kth.iv1350.util.Amount;

public class SalesLineItemTest {
    private ItemDTO chocolate;
    private SalesLineItem lineItem;

    @BeforeEach
    public void setUp() {
        chocolate = new ItemDTO("ghi789", "Luxury Chocolate",
                "Dark chocolate 70%", new Amount(50.00, "SEK"), 0.12);
        lineItem = new SalesLineItem(chocolate, 3);
    }

    @Test
    public void testGetTotalPriceCalculatesCorrectly() {
        Amount totalPrice = lineItem.getTotalPrice();
        double expected = 50.00 * 3;
        assertEquals(expected, totalPrice.getValue(), 0.01,
                "Total price should be item price multiplied by quantity.");
    }

    @Test
    public void testGetTotalVATCalculatesCorrectly() {
        Amount totalVAT = lineItem.getTotalVAT();
        double expected = 50.00 * 0.12 * 3;
        assertEquals(expected, totalVAT.getValue(), 0.01,
                "Total VAT should be price * vatRate * quantity.");
    }

    @Test
    public void testIncreaseQuantityAffectsTotalPrice() {
        lineItem.increaseQuantity(2);
        Amount totalPrice = lineItem.getTotalPrice();
        double expected = 50.00 * 5;
        assertEquals(expected, totalPrice.getValue(), 0.01,
                "Total price should reflect the increased quantity.");
    }

    @Test
    public void testIncreaseQuantityAffectsTotalVAT() {
        lineItem.increaseQuantity(2);
        Amount totalVAT = lineItem.getTotalVAT();
        double expected = 50.00 * 0.12 * 5;
        assertEquals(expected, totalVAT.getValue(), 0.01,
                "Total VAT should reflect the increased quantity.");
    }

    @Test
    public void testTotalPriceWithSingleItem() {
        SalesLineItem singleItem = new SalesLineItem(chocolate, 1);
        assertEquals(50.00, singleItem.getTotalPrice().getValue(), 0.01,
                "Total price for quantity 1 should equal the unit price.");
    }

    @Test
    public void testTotalVATWithDifferentVATRate() {
        ItemDTO lowVatItem = new ItemDTO("abc123", "Oatmeal",
                "Oatmeal 500g", new Amount(29.90, "SEK"), 0.06);
        SalesLineItem lowVatLine = new SalesLineItem(lowVatItem, 2);

        double expected = 29.90 * 0.06 * 2;
        assertEquals(expected, lowVatLine.getTotalVAT().getValue(), 0.01,
                "VAT calculation should use the item's specific VAT rate.");
    }
}
