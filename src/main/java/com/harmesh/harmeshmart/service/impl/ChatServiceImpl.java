package com.harmesh.harmeshmart.service.impl;

import com.harmesh.harmeshmart.ai.ChatProvider;
import com.harmesh.harmeshmart.ai.ChatProviderFactory;
import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.exception.ValidationException;
import com.harmesh.harmeshmart.service.ChatService;
import com.harmesh.harmeshmart.util.ValidationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of ChatService enforcing Section 17 guardrails:
 * 1. Per-session rate limit (10 messages/minute)
 * 2. Input length cap (500 characters)
 * 3. In-memory per-session question response caching
 */
public class ChatServiceImpl implements ChatService {

    private static final Logger logger = LoggerFactory.getLogger(ChatServiceImpl.class);
    private static final int MAX_INPUT_LENGTH = 500;
    private static final int MAX_MESSAGES_PER_MINUTE = 10;
    private static final long ONE_MINUTE_MS = 60 * 1000L;

    private static final String SESSION_CHAT_TIMESTAMPS = "CHAT_TIMESTAMPS";
    private static final String SESSION_CHAT_CACHE = "CHAT_CACHE";

    @Override
    public String processChatMessage(String message, String context, HttpSession session) throws AppException {
        ValidationUtil.validateRequired(message, "message");

        if (message.length() > MAX_INPUT_LENGTH) {
            throw new ValidationException("message", "Query exceeds maximum allowed length of " + MAX_INPUT_LENGTH + " characters.");
        }

        // 1. Rate Limiting per session
        if (session != null) {
            synchronized (session) {
                @SuppressWarnings("unchecked")
                LinkedList<Long> timestamps = (LinkedList<Long>) session.getAttribute(SESSION_CHAT_TIMESTAMPS);
                if (timestamps == null) {
                    timestamps = new LinkedList<>();
                    session.setAttribute(SESSION_CHAT_TIMESTAMPS, timestamps);
                }

                long now = System.currentTimeMillis();
                while (!timestamps.isEmpty() && now - timestamps.peekFirst() > ONE_MINUTE_MS) {
                    timestamps.pollFirst();
                }

                if (timestamps.size() >= MAX_MESSAGES_PER_MINUTE) {
                    throw new AppException("RATE_LIMIT_EXCEEDED", "Rate limit exceeded (10 messages/minute). Please wait a few seconds before trying again.");
                }

                timestamps.addLast(now);
            }
        }

        // 2. In-memory Per-Session Caching
        String normalizedMsg = message.trim().toLowerCase();
        if (session != null) {
            @SuppressWarnings("unchecked")
            Map<String, String> cache = (Map<String, String>) session.getAttribute(SESSION_CHAT_CACHE);
            if (cache == null) {
                cache = new ConcurrentHashMap<>();
                session.setAttribute(SESSION_CHAT_CACHE, cache);
            }

            if (cache.containsKey(normalizedMsg)) {
                logger.debug("Serving AI response from session cache for query: {}", normalizedMsg);
                return cache.get(normalizedMsg);
            }
        }

        // 3. Invoke Provider
        ChatProvider provider = ChatProviderFactory.getProvider();
        String reply = provider.getReply(message, context);

        // Save in cache
        if (session != null) {
            @SuppressWarnings("unchecked")
            Map<String, String> cache = (Map<String, String>) session.getAttribute(SESSION_CHAT_CACHE);
            if (cache != null) {
                cache.put(normalizedMsg, reply);
            }
        }

        return reply;
    }
}
