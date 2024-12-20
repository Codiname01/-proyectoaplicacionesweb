package com.cibertec.app.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false) // Garantiza unicidad y que no sea nulo
    private String name;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() { // El nombre del getter ahora coincide
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Sobrescribir equals y hashCode para evitar duplicados en Sets
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    // Método toString para facilitar la depuración
    @Override
    public String toString() {
        return "Role{id=" + id + ", name='" + name + "'}";
    }
}
