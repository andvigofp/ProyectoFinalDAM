package com.helpdeskpro.helpdesk_pro.Configuracion;

import com.helpdeskpro.helpdesk_pro.Seguridad.FiltroAutenticacionJwt;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class ConfiguracionSeguridad {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Permisos según el rol, tal como se define en el anteproyecto:
    // - Empleado: crea incidencias y solo accede a las suyas.
    // - Técnico: consulta todas, se le asignan incidencias, cambia
    //   estados y registra soluciones.
    // - Administrador: además gestiona usuarios, roles, categorías
    //   y departamentos.
    @Bean
    public SecurityFilterChain filtroCadena(
            HttpSecurity http,
            FiltroAutenticacionJwt filtroJwt) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Inicio de sesión abierto
                        .requestMatchers("/api/auth/login").permitAll()

                        // Gestión de usuarios: solo administrador
                        .requestMatchers("/api/usuarios/**")
                        .hasRole("ADMINISTRADOR")

                        // Ver todas las incidencias: técnico y administrador
                        .requestMatchers(HttpMethod.GET, "/api/incidencias")
                        .hasAnyRole("TECNICO", "ADMINISTRADOR")

                        // Acciones de técnico: asignar, cambiar estado,
                        // registrar solución
                        .requestMatchers(
                                "/api/incidencias/*/asignar/**",
                                "/api/incidencias/*/estado/**",
                                "/api/incidencias/*/solucion")
                        .hasAnyRole("TECNICO", "ADMINISTRADOR")

                        // Reabrir: el empleado (su propia incidencia, se
                        // comprueba en el servicio), técnico y administrador
                        .requestMatchers("/api/incidencias/*/reabrir")
                        .hasAnyRole("EMPLEADO", "TECNICO", "ADMINISTRADOR")

                        // Lo demás (crear incidencia, comentarios,
                        // historial, ver una incidencia...): requiere login
                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(puntoEntradaNoAutenticado())
                        .accessDeniedHandler(accesoDenegado()))
                .addFilterBefore(
                        filtroJwt, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // Respuesta 403 en JSON cuando el usuario está autenticado
    // pero su rol no le permite la operación
    @Bean
    public org.springframework.security.web.access.AccessDeniedHandler accesoDenegado() {
        return (request, response, excepcion) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    "{\"estado\":403,\"error\":\"Prohibido\","
                            + "\"mensaje\":\"Tu rol no tiene permiso "
                            + "para esta operación\"}"
            );
        };
    }

    // Respuesta 401 en JSON cuando no se envía token o es inválido
    @Bean
    public AuthenticationEntryPoint puntoEntradaNoAutenticado() {
        return (request, response, excepcion) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(
                    "{\"estado\":401,\"error\":\"No autorizado\","
                            + "\"mensaje\":\"Debes iniciar sesión con un "
                            + "token JWT válido\"}"
            );
        };
    }
}
