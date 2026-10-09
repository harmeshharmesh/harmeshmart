package com.harmesh.harmeshmart.service.payment;

/**
 * Factory for retrieving payment strategies.
 */
public final class PaymentStrategyFactory {

    private static final PaymentStrategy UPI = new UpiPaymentStrategy();
    private static final PaymentStrategy CARD = new CardPaymentStrategy();
    private static final PaymentStrategy COD = new CodPaymentStrategy();

    private PaymentStrategyFactory() {
    }

    public static PaymentStrategy getStrategy(String method) {
        if (method == null) return COD;
        String clean = method.trim().toUpperCase();
        if (clean.contains("UPI")) return UPI;
        if (clean.contains("CARD") || clean.contains("CREDIT") || clean.contains("DEBIT")) return CARD;
        return COD;
    }
}
