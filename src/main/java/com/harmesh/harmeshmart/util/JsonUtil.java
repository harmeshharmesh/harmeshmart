package com.harmesh.harmeshmart.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.harmesh.harmeshmart.dto.ApiResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

/**
 * JSON serialization utility powered by Google Gson.
 * Conforms to Section 13 API Contract Standards.
 */
public final class JsonUtil {

    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeAdapter(Timestamp.class, (com.google.gson.JsonSerializer<Timestamp>) (src, typeOfSrc, context) ->
                    new com.google.gson.JsonPrimitive(src.toString()))
            .create();

    private JsonUtil() {
    }

    public static Gson getGson() {
        return GSON;
    }

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    /**
     * Send standard JSON envelope response with specified HTTP status code.
     * Conforms to Section 13 point 2 and 3.
     */
    public static void sendJsonResponse(HttpServletResponse response, int statusCode, ApiResponse<?> body) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(GSON.toJson(body));
        out.flush();
    }
}
