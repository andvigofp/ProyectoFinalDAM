package com.helpdeskpro.helpdesk_pro.Seguridad;

import com.helpdeskpro.helpdesk_pro.Entidades.*;
import com.helpdeskpro.helpdesk_pro.Repositorio.UsuariosRepositorio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SeguridadServicioTest {

    @Mock
    private UsuariosRepositorio usuariosRepositorio;

    @InjectMocks
    private SeguridadServicio seguridadServicio;

    private Usuarios empleadoCarlos;
    private Usuarios tecnicoLaura;
    private Incidencias incidenciaDeCarlos;
    private Incidencias incidenciaDeOtro;

    private Usuarios crearUsuario(Long id, String nombre, String apellidos,
                                  String rolNombre, Long rolId) {
        return Usuarios.builder()
                .id(id)
                .nombre(nombre)
                .apellidos(apellidos)
                .email(nombre.toLowerCase() + "@helpdesk.com")
                .passwordHash("hash_bcrypt")
                .activo(true)
                .roles(Roles.builder().id(rolId).nombre(rolNombre).build())
                .build();
    }

    // Simula que la petición HTTP la hace el usuario indicado
    private void autenticarComo(Usuarios usuario) {
        DetallesUsuario detalles = new DetallesUsuario(usuario);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                        detalles, null, detalles.getAuthorities())
        );
    }

    @BeforeEach
    void prepararDatos() {
        empleadoCarlos = crearUsuario(
                3L, "Carlos", "Martín", "Empleado", 3L);
        tecnicoLaura = crearUsuario(
                2L, "Laura", "García", "Técnico", 2L);

        incidenciaDeCarlos = Incidencias.builder()
                .id(1L)
                .titulo("PC no enciende")
                .descripcion("No responde")
                .usuarios(empleadoCarlos)
                .categorias(Categorias.builder().id(1L).nombre("Hardware").build())
                .prioridades(Prioridades.builder().id(3L).nombre("Alta").build())
                .estados(Estados.builder().id(1L).nombre("Abierta").build())
                .build();

        incidenciaDeOtro = Incidencias.builder()
                .id(2L)
                .titulo("Sin Internet")
                .descripcion("No hay conexión")
                .usuarios(crearUsuario(4L, "Marta", "Ruiz", "Empleado", 3L))
                .categorias(Categorias.builder().id(3L).nombre("Red").build())
                .prioridades(Prioridades.builder().id(4L).nombre("Urgente").build())
                .estados(Estados.builder().id(2L).nombre("En proceso").build())
                .build();
    }

    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void sinAutenticacionLanzaExcepcion() {
        SecurityContextHolder.clearContext();

        assertThrows(AccessDeniedException.class,
                () -> seguridadServicio.obtenerUsuarioActual());
    }

    @Test
    void usuarioAutenticadoSeRecuperaDeLaBaseDeDatos() {
        autenticarComo(tecnicoLaura);
        when(usuariosRepositorio.findById(2L))
                .thenReturn(Optional.of(tecnicoLaura));

        Usuarios actual = seguridadServicio.obtenerUsuarioActual();

        assertEquals("laura@helpdesk.com", actual.getEmail());
    }

    @Test
    void empleadoNoAccedeAIncidenciaAjena() {
        autenticarComo(empleadoCarlos);
        when(usuariosRepositorio.findById(3L))
                .thenReturn(Optional.of(empleadoCarlos));

        assertThrows(AccessDeniedException.class,
                () -> seguridadServicio
                        .verificarAccesoIncidencia(incidenciaDeOtro));
    }

    @Test
    void empleadoAccedeASuPropiaIncidencia() {
        autenticarComo(empleadoCarlos);
        when(usuariosRepositorio.findById(3L))
                .thenReturn(Optional.of(empleadoCarlos));

        assertDoesNotThrow(() -> seguridadServicio
                .verificarAccesoIncidencia(incidenciaDeCarlos));
    }

    @Test
    void tecnicoAccedeACualquierIncidencia() {
        autenticarComo(tecnicoLaura);
        when(usuariosRepositorio.findById(2L))
                .thenReturn(Optional.of(tecnicoLaura));

        assertDoesNotThrow(() -> seguridadServicio
                .verificarAccesoIncidencia(incidenciaDeCarlos));
        assertDoesNotThrow(() -> seguridadServicio
                .verificarAccesoIncidencia(incidenciaDeOtro));
    }

    @Test
    void codigoRolDeLosTresRolesDelAnteproyecto() {
        assertEquals("EMPLEADO",
                JwtServicio.codigoRol(empleadoCarlos));
        assertEquals("TECNICO",
                JwtServicio.codigoRol(tecnicoLaura));
    }
}
