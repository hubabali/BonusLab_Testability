package org.example;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.mockito.Mockito.*;



// this test checks that sap.send() runs for each low-valued invoice.
// if there are no invoices, sap.send() should never be called.
public class SAP_BasedInvoiceSenderTest {

    // this test makes sure that sap.send() is called for every low valued invoice
    @Test
    void testWhenLowInvoicesSent() {
        // mock the dependencies
        FilterInvoice filter = mock(FilterInvoice.class);
        SAP sap = mock(SAP.class);

        // fake lowvalued invoices
        List<Invoice> lowInvoices = List.of(
                new Invoice("A", 50),
                new Invoice("B", 75)
        );

        // stub the filter to return fake invoices
        when(filter.lowValueInvoices()).thenReturn(lowInvoices);

        // inject mock into sender
        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(filter, sap);

        // run the method
        sender.sendLowValuedInvoices();

        // verify sap.send() called once per invoice
        verify(sap, times(2)).send(any(Invoice.class));
    }

    // this test makes sure no sap.send() calls happen when there are no invoices
    @Test
    void testWhenNoInvoices() {
        FilterInvoice filter = mock(FilterInvoice.class);
        SAP sap = mock(SAP.class);

        // stub to return empty list
        when(filter.lowValueInvoices()).thenReturn(List.of());

        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(filter, sap);
        sender.sendLowValuedInvoices();

        // verify that send() was never called
        verify(sap, never()).send(any(Invoice.class));
    }
    // this test fakes sap.send() failing for one invoice but checks that we keep sending others
    @Test
    void testThrowExceptionWhenBadInvoice() {
        // mock dependencies
        FilterInvoice filter = mock(FilterInvoice.class);
        SAP sap = mock(SAP.class);

        // create a bad invoice and a good one
        Invoice bad = new Invoice("BAD", 30);
        Invoice good = new Invoice("GOOD", 40);
        List<Invoice> invoices = List.of(bad, good);

        // stub the filter to return our fake list
        when(filter.lowValueInvoices()).thenReturn(invoices);

        // make sap.send() throw for the bad invoice
        doThrow(new FailToSendSAPInvoiceException("SAP send failed"))
                .when(sap).send(bad);

        // inject mocks into sender
        SAP_BasedInvoiceSender sender = new SAP_BasedInvoiceSender(filter, sap);

        // run the method, it should keep going and return the failed ones
        List<Invoice> failed = sender.sendLowValuedInvoices();

        // verify that exactly one failed
        org.junit.jupiter.api.Assertions.assertEquals(1, failed.size());
        org.junit.jupiter.api.Assertions.assertEquals("BAD", failed.get(0).getCustomer());
    }

}
