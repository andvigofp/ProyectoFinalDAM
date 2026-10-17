package com.helpdeskpro.helpdesk_pro.Seguridad;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.helpdeskpro.helpdesk_pro.Entidades.Usuarios;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtServicio {

    private final String secreto;
    private final long duracionMs;

    public JwtServicio(
            @Value("${jwt.secreto}") String secreto,
            @Value("${jwt.duracion-ms}") long duracionMs) {

        this.secreto = secreto;
        this.duracionMs = duracionMs;
    }

    // Generar un token con el email, el id y el rol del usuario
    public String generarToken(Usuarios usuario) {
        return JWT.create()
                .withSubject(usuario.getEmail())
                .withClaim("id", usuario.getId())
                .withClaim("rol", codigoRol(usuario))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(
                        System.currentTimeMillis() + duracionMs))
                .sign(Algorithm.HMAC256(secreto));
    }

    // Obtener el email guardado en el token
    public String extraerEmail(String token) {
        return verificar(token).getSubject();
    }

    // Comprobar que el token es válido y no ha caducado
    public boolean esTokenValido(String token) {
        try {
            verificar(token);
            return true;
        } catch (JWTVerificationException excepcion) {
            return false;
        }
    }

    private DecodedJWT verificar(String token) {
        return JWT.require(Algorithm.HMAC256(secreto))
                .build()
                .verify(token);
    }

    // Convertir el nombre del rol de la BD en código de seguridad:
    // "Administrador" -> ADMINISTRADOR, "Técnico" -> TECNICO,
    // "Empleado" -> EMPLEADO
    public static String codigoRol(Usuarios usuario) {
        return switch (usuario.getRoles().getNombre()) {
            case "Administrador" -> "ADMINISTRADOR";
            case "Técnico" -> "TECNICO";
            default -> "EMPLEADO";
        };
    }
}
