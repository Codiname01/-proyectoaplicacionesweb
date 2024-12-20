package com.cibertec.app.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    // Llave secreta para firmar los tokens JWT
    private SecretKey secretKey;

    @Value("${jwt.secret}")
    private String jwtSecret;

    // Duración de validez del token en milisegundos
    @Value("${jwt.expiration}")
    private long jwtExpiration;

    /**
     * Inicializar la clave secreta
     */
    @PostConstruct
    public void init() {
        // Si el valor de jwtSecret no cumple con los requisitos de longitud, se genera una clave válida
        if (jwtSecret.length() < 64) {
            this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        } else {
            this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        }
    }

    /**
     * Método para crear un token JWT
     * @param email Correo electrónico del usuario
     * @param roles Lista de roles asociados al usuario
     * @return Token JWT generado
     */
    public String createToken(String email, List<String> roles) {
        return Jwts.builder()
                .setSubject(email) // Asignar el email como sujeto del token
                .claim("roles", roles) // Incluir los roles como un claim
                .setIssuedAt(new Date()) // Fecha de emisión del token
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Fecha de expiración
                .signWith(secretKey, SignatureAlgorithm.HS512) // Firmar el token con HS512 y la clave secreta
                .compact();
    }

    /**
     * Método para validar un token JWT
     * @param token Token JWT a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token); // Validar la firma del token
            return true;
        } catch (Exception e) {
            return false; // Retornar false si el token no es válido o ha expirado
        }
    }

    /**
     * Método para obtener el email del usuario desde un token JWT
     * @param token Token JWT
     * @return Email contenido en el token
     */
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey) // Validar la clave secreta
                .build()
                .parseClaimsJws(token) // Parsear el token
                .getBody() // Obtener el cuerpo del token
                .getSubject(); // Extraer el sujeto (email)
    }
}
