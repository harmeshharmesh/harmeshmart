package com.harmesh.harmeshmart.service;

import com.harmesh.harmeshmart.exception.AppException;

import javax.servlet.http.HttpSession;

/**
 * Service orchestrating AI Chatbot queries with guardrails (rate limit, caching, input length).
 */
public interface ChatService {

    String processChatMessage(String message, String context, HttpSession session) throws AppException;
}
