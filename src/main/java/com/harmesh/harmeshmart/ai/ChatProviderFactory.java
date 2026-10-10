package com.harmesh.harmeshmart.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Properties;

/**
 * Factory for creating ChatProvider based on configuration.
 * Conforms to Section 17 Point 2.
 */
public final class ChatProviderFactory {

    private static final Logger logger = LoggerFactory.getLogger(ChatProviderFactory.class);
    private static volatile ChatProvider provider;

    private ChatProviderFactory() {
    }

    public static synchronized ChatProvider getProvider() {
        if (provider != null) {
            return provider;
        }

        String type = "mock";
        String key = "";
        try (InputStream in = ChatProviderFactory.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                Properties p = new Properties();
                p.load(in);
                type = p.getProperty("ai.chatbot.provider", "mock");
                key = p.getProperty("ai.chatbot.gemini.key", "");
            }
        } catch (Exception e) {
            logger.warn("Could not read ai.chatbot.provider from config.properties: {}", e.getMessage());
        }

        // Allow environment variables to override for cloud platforms (Render, Heroku, etc.)
        String envType = System.getenv("AI_CHATBOT_PROVIDER");
        if (envType != null && !envType.trim().isEmpty()) {
            type = envType.trim();
        }
        String envKey = System.getenv("GEMINI_API_KEY");
        if (envKey == null || envKey.trim().isEmpty()) {
            envKey = System.getenv("AI_CHATBOT_GEMINI_KEY");
        }
        if (envKey != null && !envKey.trim().isEmpty()) {
            key = envKey.trim();
        }

        if ("gemini".equalsIgnoreCase(type)) {
            provider = new GeminiChatProvider(key);
            logger.info("Initialized GeminiChatProvider.");
        } else {
            provider = new MockChatProvider();
            logger.info("Initialized MockChatProvider.");
        }

        return provider;
    }
}
