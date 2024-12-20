package com.cibertec.app.controller;

import com.cibertec.app.model.Role;
import com.cibertec.app.model.Usuario;
import com.cibertec.app.repository.RoleRepository;
import com.cibertec.app.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UsuarioRepository usuarioRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ========== REGISTRO ==========

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "register"; // Vista: register.html
    }

    @PostMapping("/register")
    public String registerUser(Usuario usuario, RedirectAttributes redirectAttributes) {
        try {
            // Validar formato del correo
            if (!usuario.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
                redirectAttributes.addFlashAttribute("error", "El correo tiene un formato inválido.");
                return "redirect:/register";
            }

            // Validar longitud de la contraseña
            if (usuario.getPassword().length() < 6) {
                redirectAttributes.addFlashAttribute("error", "La contraseña debe tener al menos 6 caracteres.");
                return "redirect:/register";
            }

            // Verificar si el correo ya existe
            if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El correo ya está registrado.");
                return "redirect:/register";
            }

            // Asignar rol USER por defecto
            Role userRole = roleRepository.findByName("ROLE_USER");
            if (userRole == null) {
                userRole = new Role();
                userRole.setName("ROLE_USER");
                roleRepository.save(userRole);
            }

            usuario.getRoles().add(userRole);

            // Encriptar la contraseña
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

            // Guardar el usuario
            usuarioRepository.save(usuario);
            redirectAttributes.addFlashAttribute("success", "Usuario registrado exitosamente.");
            return "redirect:/login";

        } catch (Exception e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            redirectAttributes.addFlashAttribute("error",
                    "Ocurrió un error inesperado al registrar el usuario.");
            return "redirect:/register";
        }
    }

    // ========== INICIO DE SESIÓN ==========

    // Página de inicio de sesión (GET)
    @GetMapping("/login")
    public String login() {
        // Simplemente devuelve la vista login.html
        // El POST /login lo maneja Spring Security con formLogin
        return "login";
    }

    /*
    // Si QUIERES manejar el login manual, descomenta este método,
    // pero ENTONCES comenta el .formLogin() en SecurityConfig
    // para evitar choques de lógica en la misma URL.

    @PostMapping("/login")
    public String loginManual(@RequestParam String email,
                              @RequestParam String password,
                              RedirectAttributes redirectAttributes) {
        try {
            // Aquí iría la lógica manual de autenticación,
            // pero está desactivada al usar formLogin de Spring Security.
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Ocurrió un error inesperado. Intente nuevamente.");
            return "redirect:/login";
        }
    }
    */
}
