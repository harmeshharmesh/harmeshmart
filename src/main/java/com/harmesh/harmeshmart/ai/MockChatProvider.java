package com.harmesh.harmeshmart.ai;

import java.util.HashMap;
import java.util.Map;

/**
 * Mock AI Chat Provider supplying intelligent canned domain responses without requiring external network calls.
 * Conforms to Section 11 & Section 17 specifications.
 */
public class MockChatProvider implements ChatProvider {

    private static final Map<String, String> FAQ_ANSWERS = new HashMap<>();

    static {
        FAQ_ANSWERS.put("return", "HarmeshMart offers a hassle-free 7-day replacement & return guarantee on all eligible products! Simply visit your Orders page and select 'Return Item'.");
        FAQ_ANSWERS.put("refund", "Refunds are processed automatically to your original payment method (or UPI account) within 24 to 48 hours of item pickup.");
        FAQ_ANSWERS.put("shipping", "Standard delivery takes 2-4 business days across India. HarmeshMart Express Prime delivery delivers within 24 hours to major metro areas!");
        FAQ_ANSWERS.put("delivery", "Orders placed before 2:00 PM IST are dispatched the same day from our nearest merchant fulfillment hub.");
        FAQ_ANSWERS.put("payment", "We accept UPI (Google Pay, PhonePe, Paytm), All major Credit/Debit Cards (Visa, Mastercard, RuPay), and Cash on Delivery (COD).");
        FAQ_ANSWERS.put("seller", "To sell on HarmeshMart, click 'Become a Seller' in the top header. Register your store name, provide your contact details, and start listing products immediately!");
        FAQ_ANSWERS.put("track", "You can track your package live directly from the 'Orders' tab in your top navigation menu.");
        FAQ_ANSWERS.put("coupon", "Use promo code 'SAVE10' at checkout to receive an instant ₹100 flat discount on your shopping cart!");
        FAQ_ANSWERS.put("phone", "We feature the flagship UltraPhone 15 Pro 5G with a 120Hz Super AMOLED display, 48MP triple camera, and 65W Turbo charging for ₹49,999!");
        FAQ_ANSWERS.put("headphone", "Check out the SonicWave Pro Wireless Headphones with 40-hour battery and Active Noise Cancellation (ANC) for just ₹8,999.");
        FAQ_ANSWERS.put("warranty", "All electronics on HarmeshMart come with a minimum 1-Year Official Manufacturer Warranty plus 7-day seller replacement.");
    }

    @Override
    public String getReply(String userMessage, String context) {
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return "Hello! I am your HarmeshMart AI Assistant. How can I help you find products, track orders, or answer seller queries today?";
        }

        String msg = userMessage.toLowerCase();

        for (Map.Entry<String, String> entry : FAQ_ANSWERS.entrySet()) {
            if (msg.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        if (msg.contains("hi") || msg.contains("hello") || msg.contains("hey")) {
            return "Hi there! Welcome to HarmeshMart! You can ask me about order tracking, delivery timelines, payment options, coupons, or product recommendations.";
        }

        if (msg.contains("espresso") || msg.contains("coffee")) {
            return "Our BaristaTouch 19-Bar Espresso Machine (₹14,999) features Italian thermo-block rapid heating and a steam wand for silky micro-foam lattes!";
        }

        if (msg.contains("keyboard")) {
            return "Take a look at the AeroMech RGB Hot-Swappable Mechanical Keyboard with Gateron switches for ₹3,499!";
        }

        return "Thank you for asking about HarmeshMart! We offer premium Electronics, Fashion, Home Kitchen, Books, and Fitness equipment with fast delivery across India. Use code 'SAVE10' for extra savings!";
    }
}
