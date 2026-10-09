package com.harmesh.harmeshmart.dto;

/**
 * Chat request DTO conforming to Section 11 & 17:
 * POST /api/v1/chat { "message": "..." }
 */
public class ChatRequestDTO {
    private String message;
    private String context;

    public ChatRequestDTO() {
    }

    public ChatRequestDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
