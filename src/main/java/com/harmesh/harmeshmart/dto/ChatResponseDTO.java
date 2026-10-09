package com.harmesh.harmeshmart.dto;

/**
 * Chat response DTO conforming to Section 11 & 17:
 * { "reply": "..." }
 */
public class ChatResponseDTO {
    private String reply;
    private String provider;

    public ChatResponseDTO() {
    }

    public ChatResponseDTO(String reply, String provider) {
        this.reply = reply;
        this.provider = provider;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
