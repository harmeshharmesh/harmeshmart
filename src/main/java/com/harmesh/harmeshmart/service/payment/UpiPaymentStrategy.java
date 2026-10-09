package com.harmesh.harmeshmart.service.payment;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Strategy implementation for UPI QR / VPA Mock Payment.
 */
public class UpiPaymentStrategy implements PaymentStrategy {

    @Override
    public String processPayment(Long orderId, BigDecimal amount, String paymentDetails) {
        String ref = "UPI-REF-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        return "SUCCESS: Simulated UPI Payment of ₹" + amount + " verified via " +
                (paymentDetails != null ? paymentDetails : "HarmeshMart@okhdfcbank") + ". Ref: " + ref;
    }

    @Override
    public String getMethodName() {
        return "UPI";
    }
}
