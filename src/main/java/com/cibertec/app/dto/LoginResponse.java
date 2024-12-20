package com.cibertec.app.dto;

public class LoginResponse {
    private String token;
    private String message;

    public LoginResponse(String token) {
        this.token = token;
        this.message = "Login exitoso";
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
