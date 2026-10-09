package com.harmesh.harmeshmart.controller;

import com.harmesh.harmeshmart.dto.HealthResponseDTO;
import com.harmesh.harmeshmart.util.DBUtil;
import com.harmesh.harmeshmart.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Health check endpoint conforming to Section 18 Observability Requirements:
 * GET /api/v1/health returns { "status": "UP", "db": "UP" }
 */
@WebServlet(name = "HealthServlet", urlPatterns = {"/api/v1/health"})
public class HealthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String dbStatus = "DOWN";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1");
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                dbStatus = "UP";
            }
        } catch (Exception e) {
            dbStatus = "DOWN";
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        HealthResponseDTO health = new HealthResponseDTO("UP", dbStatus);
        resp.getWriter().print(JsonUtil.toJson(health));
    }
}
