package com.cibertec.app.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre; // Campo obligatorio.

    @Column(unique = true, nullable = false)
    private String email; // Evita correos duplicados.

    @Column(nullable = false)
    private String password; // Se usa como campo unificado para la contraseña.

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Venta> ventas = new HashSet<>();

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Carrito> carritos = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_role",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @Column(name = "password_encriptada", nullable = false)
    private boolean passwordEncriptada = false;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(Set<Venta> ventas) {
        this.ventas = ventas;
    }

    public Set<Carrito> getCarritos() {
        return carritos;
    }

    public void setCarritos(Set<Carrito> carritos) {
        this.carritos = carritos;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isPasswordEncriptada() {
        return passwordEncriptada;
    }

    public void setPasswordEncriptada(boolean passwordEncriptada) {
        this.passwordEncriptada = passwordEncriptada;
    }

    // Método para verificar si el usuario es ADMIN
    public boolean isAdmin() {
        return this.roles.stream()
                .anyMatch(role -> role.getName().equalsIgnoreCase("ROLE_ADMIN"));
    }
}
