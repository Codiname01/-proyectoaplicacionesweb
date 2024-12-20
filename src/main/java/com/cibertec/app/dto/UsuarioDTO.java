package com.cibertec.app.dto;

public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private String password; // Agregar este atributo

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() { // Getter para password
        return password;
    }

    public void setPassword(String password) { // Setter para password
        this.password = password;
    }
}
