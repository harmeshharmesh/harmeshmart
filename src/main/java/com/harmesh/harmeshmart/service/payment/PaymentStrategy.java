package com.harmesh.harmeshmart.service.payment;

import java.math.BigDecimal;

/**
 * Strategy pattern interface for mock payment confirmation.
 * Conforms to Section 12 Required Design Patterns:
 * "Strategy (swappable notification/payment-mock channels)".
 */
public interface PaymentStrategy {

    /**
     * Process mock payment for the specified amount.
     *
     * @param orderId ID of the order being processed
     * @param amount Total amount to charge
     * @param paymentDetails Payment reference or mock input
     * @return Payment confirmation result string or transaction reference
     */
    String processPayment(Long orderId, BigDecimal amount, String paymentDetails);

    /**
     * Returns the payment method identifier.
     */
    String getMethodName();
}
