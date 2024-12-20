package com.cibertec.app.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cibertec.app.model.Role;
import com.cibertec.app.model.Usuario;
import com.cibertec.app.repository.RoleRepository;
import com.cibertec.app.repository.UsuarioRepository;

@Component
public class PasswordUpdater implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String email = "arenski21@gmail.com";
        String rawPassword = "16121770";

        // Buscar usuario administrador
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        Usuario usuario;

        if (usuarioOpt.isPresent()) {
            usuario = usuarioOpt.get();

            // Encriptar contraseña si no lo está
            if (!usuario.getPassword().startsWith("$2a$")) { // BCrypt empieza con $2a$
                usuario.setPassword(passwordEncoder.encode(rawPassword));
                usuarioRepository.save(usuario);
                System.out.println("Contraseña del administrador encriptada.");
            }
        } else {
            // Crear el usuario administrador si no existe
            usuario = new Usuario();
            usuario.setEmail(email);
            usuario.setNombre("Administrador");
            usuario.setPassword(passwordEncoder.encode(rawPassword));

            // Asignar rol ADMIN
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            if (adminRole == null) {
                adminRole = new Role();
                adminRole.setName("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }
            usuario.getRoles().add(adminRole);

            usuarioRepository.save(usuario);
            System.out.println("Usuario administrador creado y contraseña encriptada.");
        }
    }
}
