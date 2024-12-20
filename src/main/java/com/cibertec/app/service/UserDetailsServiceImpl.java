package com.cibertec.app.service;

import com.cibertec.app.model.Usuario;
import com.cibertec.app.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (email == null || email.trim().isEmpty()) {
            logger.error("Intento de inicio de sesión con email vacío");
            throw new UsernameNotFoundException("El email no puede estar vacío");
        }

        logger.debug("Intentando cargar usuario con email: '{}'", email);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("No se encontró usuario con email: '{}'", email);
                    return new UsernameNotFoundException("Usuario no encontrado con email: " + email);
                });

        logger.info("Usuario encontrado: '{}'", usuario.getEmail());
        logger.debug("Roles del usuario: {}", usuario.getRoles());

        // Construcción de UserDetails con formato seguro de roles
        return User.withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(usuario.getRoles().stream()
                        .map(role -> {
                            String roleName = role.getName();
                            if (!roleName.startsWith("ROLE_")) {
                                roleName = "ROLE_" + roleName;
                            }
                            return new SimpleGrantedAuthority(roleName);
                        })
                        .collect(Collectors.toList()))
                .build();
    }
}
