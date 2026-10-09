package com.harmesh.harmeshmart.service.payment;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Strategy implementation for Credit/Debit Card Mock Payment.
 */
public class CardPaymentStrategy implements PaymentStrategy {

    @Override
    public String processPayment(Long orderId, BigDecimal amount, String paymentDetails) {
        String last4 = (paymentDetails != null && paymentDetails.length() >= 4)
                ? paymentDetails.substring(paymentDetails.length() - 4) : "4242";
        String txn = "TXN-CARD-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        return "SUCCESS: Mock Card charge of ₹" + amount + " authorized on card ending with *" + last4 + ". Txn: " + txn;
    }

    @Override
    public String getMethodName() {
        return "CREDIT_CARD";
    }
}
