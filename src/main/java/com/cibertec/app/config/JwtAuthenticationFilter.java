package com.cibertec.app.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider,
                                   UserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Determina qué rutas NO deben pasar por la validación del token.
     * Aquí es donde agregamos "/api/auth/login" y "/api/auth/register"
     * para que no exija JWT en esos endpoints.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.equals("/")
                || path.startsWith("/login")
                || path.startsWith("/register")
                || path.startsWith("/api/auth/login")      // <--- ¡Clave!
                || path.startsWith("/api/auth/register")   // <--- ¡Clave!
                || path.equals("/favicon.ico")
                || path.startsWith("/static");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        logger.debug("Iniciando JwtAuthenticationFilter para la URL: {}", request.getRequestURI());

        try {
            // Extraer el token del header Authorization
            String token = getTokenFromRequest(request);

            // Validar si el token es nulo o inválido:
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                logger.warn("Token inválido o no presente en la solicitud.");
                filterChain.doFilter(request, response);
                return;  // No autenticamos nada, dejamos que siga
            }

            // Si el token es válido, obtenemos el email del token
            String email = jwtTokenProvider.getEmailFromToken(token);
            logger.debug("Token válido. Email extraído del token: {}", email);

            // Verificar que no haya una autenticación previa en el contexto
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (userDetails != null && userDetails.isEnabled()) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    logger.debug("Usuario autenticado correctamente: {}", email);
                }
            }
        } catch (Exception e) {
            logger.error("Error al procesar el token JWT: {}", e.getMessage(), e);
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }

    /**
     * Obtiene el Bearer Token del header 'Authorization'
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        logger.debug("No se encontró un encabezado Authorization válido.");
        return null;
    }
}
