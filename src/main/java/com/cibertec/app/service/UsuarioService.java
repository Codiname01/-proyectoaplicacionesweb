package com.cibertec.app.service;

import com.cibertec.app.dto.UsuarioDTO;
import com.cibertec.app.model.Usuario;
import com.cibertec.app.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    // Método para listar todos los usuarios como DTO
    public List<UsuarioDTO> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::convertirAUsuarioDTO)
                .collect(Collectors.toList());
    }

    // Obtener un Usuario por ID y convertirlo a DTO
    public UsuarioDTO obtenerUsuarioDTOPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con id: " + id));
        return convertirAUsuarioDTO(usuario);
    }

    // Guardar un Usuario desde un UsuarioDTO
    public void guardarDesdeDTO(UsuarioDTO usuarioDTO) {
        Usuario usuario = convertirAUsuario(usuarioDTO);
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setPasswordEncriptada(true);
        usuarioRepository.save(usuario);
        logger.info("Usuario guardado: {}", usuario.getEmail());
    }

    // Actualizar un Usuario desde un UsuarioDTO
    public void actualizarDesdeDTO(UsuarioDTO usuarioDTO) {
        Usuario usuarioExistente = usuarioRepository.findById(usuarioDTO.getId())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con id: " + usuarioDTO.getId()));

        usuarioExistente.setNombre(usuarioDTO.getNombre());
        usuarioExistente.setEmail(usuarioDTO.getEmail());

        // Actualizar contraseña solo si está presente
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
            usuarioExistente.setPasswordEncriptada(true);
        }

        usuarioRepository.save(usuarioExistente);
        logger.info("Usuario actualizado: {}", usuarioExistente.getEmail());
    }

    public void eliminar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        usuarioRepository.delete(usuario);
        logger.info("Usuario eliminado con id: {}", id);
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }


    public void actualizarContraseña(String email, String nuevaContraseña) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setPassword(nuevaContraseña); // Actualiza la contraseña
            usuarioRepository.save(usuario);
            System.out.println("Contraseña actualizada correctamente.");
        } else {
            throw new RuntimeException("Usuario no encontrado con email: " + email);
        }
    }

    // Métodos auxiliares para conversión entre Usuario y UsuarioDTO
    private UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    private Usuario convertirAUsuario(UsuarioDTO usuarioDTO) {
        return modelMapper.map(usuarioDTO, Usuario.class);
    }
}
