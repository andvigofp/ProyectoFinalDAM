package com.helpdeskpro.helpdesk_pro.Seguridad;

import com.helpdeskpro.helpdesk_pro.Entidades.Roles;
import com.helpdeskpro.helpdesk_pro.Entidades.Usuarios;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServicioTest {

    private JwtServicio jwtServicio;
    private Usuarios tecnico;

    @BeforeEach
    void prepararDatos() {
        jwtServicio = new JwtServicio(
                "clave-de-prueba-de-al-menos-32-caracteres-123456",
                3600000L
        );
        tecnico = Usuarios.builder()
                .id(2L)
                .nombre("Laura")
                .apellidos("García")
                .email("laura@helpdesk.com")
                .passwordHash("hash_bcrypt")
                .activo(true)
                .roles(Roles.builder().id(2L).nombre("Técnico").build())
                .build();
    }

    @Test
    void generarYExtraerEmailDelToken() {
        String token = jwtServicio.generarToken(tecnico);

        assertNotNull(token);
        assertEquals("laura@helpdesk.com", jwtServicio.extraerEmail(token));
        assertTrue(jwtServicio.esTokenValido(token));
    }

    @Test
    void tokenManipuladoNoEsValido() {
        String token = jwtServicio.generarToken(tecnico);

        assertFalse(jwtServicio.esTokenValido(token + "caracteres-extra"));
        assertFalse(jwtServicio.esTokenValido("token-falso"));
    }

    @Test
    void codigoRolMapeaLosTresRolesDelAnteproyecto() {
        Usuarios admin = Usuarios.builder()
                .id(1L).nombre("Andrés").apellidos("Fernández")
                .email("andres@helpdesk.com").passwordHash("hash")
                .roles(Roles.builder().id(1L).nombre("Administrador").build())
                .build();
        Usuarios empleado = Usuarios.builder()
                .id(3L).nombre("Carlos").apellidos("Martín")
                .email("carlos@helpdesk.com").passwordHash("hash")
                .roles(Roles.builder().id(3L).nombre("Empleado").build())
                .build();

        assertEquals("ADMINISTRADOR", JwtServicio.codigoRol(admin));
        assertEquals("TECNICO", JwtServicio.codigoRol(tecnico));
        assertEquals("EMPLEADO", JwtServicio.codigoRol(empleado));
    }
}
