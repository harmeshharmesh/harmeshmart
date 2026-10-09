package com.harmesh.harmeshmart.ai;

/**
 * AI Provider interface conforming to Section 17:
 * Swappable interface between MockChatProvider and GeminiChatProvider.
 */
public interface ChatProvider {

    /**
     * Generate an AI assistance reply for the given user message and context.
     *
     * @param userMessage Customer message or inquiry
     * @param context Contextual details such as current product or catalog info
     * @return Formatted domain response
     */
    String getReply(String userMessage, String context);
}
