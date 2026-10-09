package com.harmesh.harmeshmart.controller;

import com.harmesh.harmeshmart.dto.ApiResponse;
import com.harmesh.harmeshmart.dto.ChatRequestDTO;
import com.harmesh.harmeshmart.dto.ChatResponseDTO;
import com.harmesh.harmeshmart.exception.AppException;
import com.harmesh.harmeshmart.exception.ValidationException;
import com.harmesh.harmeshmart.service.ChatService;
import com.harmesh.harmeshmart.service.impl.ChatServiceImpl;
import com.harmesh.harmeshmart.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * AI Chatbot REST endpoint conforming to Section 11 & Section 17.
 * Versioned under /api/v1/chat as mandated by Section 13 Point 1.
 */
@WebServlet(name = "ChatServlet", urlPatterns = {"/api/v1/chat"})
public class ChatServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ChatServlet);
    private final ChatService chatService = new ChatServiceImpl();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        String rawJson = sb.toString();
        ChatRequestDTO requestDTO;
        try {
            requestDTO = JsonUtil.fromJson(rawJson, ChatRequestDTO.class);
        } catch (Exception e) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    ApiResponse.error("BAD_REQUEST", "Malformed JSON request body."));
            return;
        }

        if (requestDTO == null || requestDTO.getMessage() == null || requestDTO.getMessage().trim().isEmpty()) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    ApiResponse.error("VALIDATION_ERROR", "Message cannot be empty."));
            return;
        }

        try {
            String reply = chatService.processChatMessage(requestDTO.getMessage(), requestDTO.getContext(), req.getSession(true));
            ChatResponseDTO responseDTO = new ChatResponseDTO(reply, "HarmeshMart AI");
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, ApiResponse.success(responseDTO));
        } catch (ValidationException ve) {
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    ApiResponse.error("VALIDATION_ERROR", ve.getMessage()));
        } catch (AppException ae) {
            if ("RATE_LIMIT_EXCEEDED".equals(ae.getErrorCode())) {
                JsonUtil.sendJsonResponse(resp, 429, ApiResponse.error(ae.getErrorCode(), ae.getMessage()));
            } else {
                JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        ApiResponse.error(ae.getErrorCode(), ae.getMessage()));
            }
        } catch (Exception ex) {
            logger.error("ChatServlet error: {}", ex.getMessage(), ex);
            // Graceful degraded response (Section 11 Point 3: return static degraded response)
            ChatResponseDTO fallback = new ChatResponseDTO(
                    "I am currently experiencing higher than normal traffic. Please check our FAQ or contact support@harmeshmart.com for assistance.",
                    "HarmeshMart Fallback");
            JsonUtil.sendJsonResponse(resp, HttpServletResponse.SC_OK, ApiResponse.success(fallback));
        }
    }
}
