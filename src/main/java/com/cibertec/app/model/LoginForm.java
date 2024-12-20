package com.cibertec.app.model;

public class LoginForm {
    private String email;
    private String password;

    // Constructor vacío
    public LoginForm() {
    }

    // Constructor con todos los campos
    public LoginForm(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters y setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Método toString() para debugging
    @Override
    public String toString() {
        return "LoginForm{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}