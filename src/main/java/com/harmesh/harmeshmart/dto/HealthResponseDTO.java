package com.harmesh.harmeshmart.dto;

/**
 * Health check response DTO conforming to Section 18:
 * GET /api/v1/health returns { "status": "UP", "db": "UP" }
 */
public class HealthResponseDTO {
    private String status;
    private String db;
    private long timestamp;

    public HealthResponseDTO() {
    }

    public HealthResponseDTO(String status, String db) {
        this.status = status;
        this.db = db;
        this.timestamp = System.currentTimeMillis();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
