package org.example;

// custom exception thrown when an invoice fails to send to SAP
public class FailToSendSAPInvoiceException extends RuntimeException {
    public FailToSendSAPInvoiceException(String message) {
        super(message);
    }
}
