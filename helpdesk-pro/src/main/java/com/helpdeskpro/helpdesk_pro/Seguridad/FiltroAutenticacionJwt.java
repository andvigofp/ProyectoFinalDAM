package com.helpdeskpro.helpdesk_pro.Seguridad;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Intercepta cada petición y, si lleva un token JWT válido,
// marca al usuario como autenticado en el contexto de seguridad
@Component
public class FiltroAutenticacionJwt extends OncePerRequestFilter {

    private final JwtServicio jwtServicio;
    private final DetallesUsuarioServicio detallesUsuarioServicio;

    public FiltroAutenticacionJwt(
            JwtServicio jwtServicio,
            DetallesUsuarioServicio detallesUsuarioServicio) {

        this.jwtServicio = jwtServicio;
        this.detallesUsuarioServicio = detallesUsuarioServicio;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String cabecera = request.getHeader("Authorization");

        if (cabecera == null || !cabecera.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = cabecera.substring(7);

        if (jwtServicio.esTokenValido(token)) {
            String email = jwtServicio.extraerEmail(token);
            UserDetails usuario =
                    detallesUsuarioServicio.loadUserByUsername(email);

            UsernamePasswordAuthenticationToken autenticacion =
                    new UsernamePasswordAuthenticationToken(
                            usuario, null, usuario.getAuthorities());
            autenticacion.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request));

            SecurityContextHolder.getContext()
                    .setAuthentication(autenticacion);
        }

        filterChain.doFilter(request, response);
    }
}
