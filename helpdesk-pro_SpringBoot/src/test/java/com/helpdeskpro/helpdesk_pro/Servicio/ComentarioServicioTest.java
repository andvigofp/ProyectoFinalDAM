package com.helpdeskpro.helpdesk_pro.Servicio;

import com.helpdeskpro.helpdesk_pro.DTO.ComentarioRequestDTO;
import com.helpdeskpro.helpdesk_pro.DTO.ComentarioResponseDTO;
import com.helpdeskpro.helpdesk_pro.Entidades.*;
import com.helpdeskpro.helpdesk_pro.Excepciones.RecursoNoEncontradoException;
import com.helpdeskpro.helpdesk_pro.Excepciones.SolicitudIncorrectaException;
import com.helpdeskpro.helpdesk_pro.Repositorio.ComentariosRepositorio;
import com.helpdeskpro.helpdesk_pro.Repositorio.HistorialesRepositorio;
import com.helpdeskpro.helpdesk_pro.Repositorio.IncidenciasRepositorio;
import com.helpdeskpro.helpdesk_pro.Seguridad.SeguridadServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComentarioServicioTest {

    @Mock
    private ComentariosRepositorio comentariosRepositorio;
    @Mock
    private IncidenciasRepositorio incidenciasRepositorio;
    @Mock
    private HistorialesRepositorio historialesRepositorio;
    @Mock
    private SeguridadServicio seguridadServicio;

    @InjectMocks
    private ComentarioServicio comentarioServicio;

    private Usuarios tecnico;
    private Incidencias incidencia;

    private Usuarios crearUsuario(Long id, String nombre, String apellidos) {
        return Usuarios.builder()
                .id(id)
                .nombre(nombre)
                .apellidos(apellidos)
                .email(nombre.toLowerCase() + "@helpdesk.com")
                .passwordHash("hash_bcrypt")
                .roles(Roles.builder().id(2L).nombre("Técnico").build())
                .build();
    }

    @BeforeEach
    void prepararDatos() {
        tecnico = crearUsuario(2L, "Laura", "García");
        incidencia = Incidencias.builder()
                .id(1L)
                .titulo("PC no enciende")
                .descripcion("No responde al pulsar el botón")
                .usuarios(crearUsuario(3L, "Carlos", "Martín"))
                .categorias(Categorias.builder().id(1L).nombre("Hardware").build())
                .prioridades(Prioridades.builder().id(3L).nombre("Alta").build())
                .estados(Estados.builder().id(1L).nombre("Abierta").build())
                .build();
    }

    @Test
    void crearComentarioGuardaElComentarioYElHistorial() {
        ComentarioRequestDTO dto = ComentarioRequestDTO.builder()
                .mensaje("Estoy revisando la incidencia.")
                .usuarioId(2L)
                .incidenciaId(1L)
                .build();

        when(incidenciasRepositorio.findById(1L))
                .thenReturn(Optional.of(incidencia));
        when(seguridadServicio.obtenerUsuarioActual()).thenReturn(tecnico);
        when(comentariosRepositorio.save(any(Comentarios.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        ComentarioResponseDTO respuesta =
                comentarioServicio.crearComentario(dto);

        assertEquals("Estoy revisando la incidencia.", respuesta.getMensaje());
        assertEquals("Laura García", respuesta.getUsuarioNombre());
        assertEquals(1L, respuesta.getIncidenciaId());
        verify(historialesRepositorio, times(1))
                .save(any(Historiales.class));
    }

    @Test
    void crearComentarioEnIncidenciaInexistenteLanzaExcepcion() {
        ComentarioRequestDTO dto = ComentarioRequestDTO.builder()
                .mensaje("Estoy revisando la incidencia.")
                .usuarioId(2L)
                .incidenciaId(99L)
                .build();

        when(incidenciasRepositorio.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> comentarioServicio.crearComentario(dto));

        verify(comentariosRepositorio, never()).save(any());
    }

    @Test
    void crearComentarioVacioLanzaExcepcion() {
        ComentarioRequestDTO dto = ComentarioRequestDTO.builder()
                .mensaje(" ")
                .usuarioId(2L)
                .incidenciaId(1L)
                .build();

        assertThrows(SolicitudIncorrectaException.class,
                () -> comentarioServicio.crearComentario(dto));
    }
}
