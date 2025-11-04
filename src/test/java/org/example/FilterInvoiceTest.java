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
    // this test uses a stubbed dao so we dont touch the real db, just fake data.
    // it checks that lowValueInvoices() an filters correctly with the stubbed list
    @org.junit.jupiter.api.Test
    void filterInvoiceStubbedTest() {
        // create a mock dao
        org.example.QueryInvoicesDAO stubDao = org.mockito.Mockito.mock(org.example.QueryInvoicesDAO.class);

        // fake invoices 1 low value, 1 high
        java.util.List<org.example.Invoice> fakeInvoices = java.util.List.of(
                new org.example.Invoice("INV-001", 50),
                new org.example.Invoice("INV-002", 250)
        );

        // stub dao.all() to return fake list
        org.mockito.Mockito.when(stubDao.all()).thenReturn(fakeInvoices);

        // inject stub into FilterInvoice
        org.example.FilterInvoice filterInvoice = new org.example.FilterInvoice(stubDao);

        // run logic
        java.util.List<org.example.Invoice> lowInvoices = filterInvoice.lowValueInvoices();

        // assertions
        org.junit.jupiter.api.Assertions.assertEquals(1, lowInvoices.size());
        org.junit.jupiter.api.Assertions.assertTrue(lowInvoices.get(0).getValue() < 100);
    }

}
