package com.cibertec.app.util; // Ajusta esto al paquete donde decidas colocar la clase

import com.cibertec.app.model.Usuario;
import com.cibertec.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioRepositoryTester implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        String email = "arenski21@gmail.com";
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        
        if (usuario.isPresent()) {
            System.out.println("Usuario encontrado: " + usuario.get().getEmail());
            System.out.println("Nombre del usuario: " + usuario.get().getNombre());
            System.out.println("Roles del usuario: " + usuario.get().getRoles());
        } else {
            System.out.println("Usuario no encontrado con email: " + email);
            // Aquí podrías decidir crear el usuario o manejarlo de otra forma
        }
    }

}