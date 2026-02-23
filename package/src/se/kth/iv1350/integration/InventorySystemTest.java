package se.kth.iv1350.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import se.kth.iv1350.dto.ItemDTO;

public class InventorySystemTest {
    private InventorySystem inventory;

    @BeforeEach
    public void setUp() {
        inventory = new InventorySystem();
    }

    @Test
    public void testRetrieveExistingItemReturnsCorrectItem() throws ItemNotFoundException {
        ItemDTO item = inventory.retrieveItemInfo("abc123");
        assertEquals("abc123", item.getItemID(),
                "Retrieved item should have the correct ID.");
        assertEquals("BigWheel Oatmeal", item.getName(),
                "Retrieved item should have the correct name.");
    }

    @Test
    public void testRetrieveNonExistentItemThrowsItemNotFoundException() {
        assertThrows(ItemNotFoundException.class,
                () -> inventory.retrieveItemInfo("nonExistentID"),
                "Searching for a non-existent item should throw ItemNotFoundException.");
    }

    @Test
    public void testDatabaseFailureItemIDThrowsDatabaseFailureException() {
        assertThrows(DatabaseFailureException.class,
                () -> inventory.retrieveItemInfo("FAIL_DB"),
                "Using the database failure item ID should throw DatabaseFailureException.");
    }

    @Test
    public void testItemNotFoundExceptionContainsCorrectMessage() {
        try {
            inventory.retrieveItemInfo("unknownItem");
            fail("Expected ItemNotFoundException was not thrown.");
        } catch (ItemNotFoundException e) {
            assertTrue(e.getMessage().contains("unknownItem"),
                    "Exception message should contain the item ID that was not found.");
        }
    }
}
