package com.harmesh.harmeshmart.dto;

/**
 * Standard API response envelope format conforming to Section 13:
 * { "success": true, "data": { ... }, "error": null }
 * { "success": false, "data": null, "error": { "code": "...", "message": "..." } }
 */
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private ApiError error;

    public ApiResponse() {
    }

    public ApiResponse(boolean success, T data, ApiError error) {
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(false, null, new ApiError(code, message));
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ApiError getError() {
        return error;
    }

    public void setError(ApiError error) {
        this.error = error;
    }
}
