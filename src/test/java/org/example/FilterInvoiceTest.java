package org.example;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


public class FilterInvoiceTest {
    // this test just runs with the real dao + database, no stubs or anything.
    // it basically checks if lowValueInvoices() only gives back invoices under 100
    @Test
    void filterInvoiceTest() {
        FilterInvoice filterInvoice = new FilterInvoice();

        // \run real filtering logic
        List<Invoice> lowValuedInvoices = filterInvoice.lowValueInvoices();

        // ensure we got valid results
        assertNotNull(lowValuedInvoices, "List of invoices should not be null");

        // verify all invoices return are below the threshold (100)
        assertTrue(
                lowValuedInvoices.stream().allMatch(inv -> inv.getValue() < 100),
                "All invoices should be below the low-value threshold"
        );
    }
}
