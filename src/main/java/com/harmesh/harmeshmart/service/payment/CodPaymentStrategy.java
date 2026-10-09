package com.harmesh.harmeshmart.service.payment;

import java.math.BigDecimal;

/**
 * Strategy implementation for Cash On Delivery (COD) mock verification.
 */
public class CodPaymentStrategy implements PaymentStrategy {

    @Override
    public String processPayment(Long orderId, BigDecimal amount, String paymentDetails) {
        return "SUCCESS: Order placed via Cash on Delivery. ₹" + amount + " payable to delivery executive upon doorstep arrival.";
    }

    @Override
    public String getMethodName() {
        return "COD";
    }
}
