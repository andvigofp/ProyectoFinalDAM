package com.helpdeskpro.helpdesk_pro.Servicio;

import com.helpdeskpro.helpdesk_pro.DTO.HistorialResponseDTO;
import com.helpdeskpro.helpdesk_pro.Entidades.*;
import com.helpdeskpro.helpdesk_pro.Excepciones.RecursoNoEncontradoException;
import com.helpdeskpro.helpdesk_pro.Excepciones.SolicitudIncorrectaException;
import com.helpdeskpro.helpdesk_pro.Repositorio.HistorialesRepositorio;
import com.helpdeskpro.helpdesk_pro.Repositorio.IncidenciasRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HistorialServicioTest {

    @Mock
    private HistorialesRepositorio historialesRepositorio;
    @Mock
    private IncidenciasRepositorio incidenciasRepositorio;

    @InjectMocks
    private HistorialServicio historialServicio;

    private Usuarios tecnico;
    private Incidencias incidencia;
    private Historiales historial;

    @BeforeEach
    void prepararDatos() {
        tecnico = Usuarios.builder()
                .id(2L)
                .nombre("Laura")
                .apellidos("García")
                .email("laura@helpdesk.com")
                .passwordHash("hash_bcrypt")
                .roles(Roles.builder().id(2L).nombre("Técnico").build())
                .build();
        incidencia = Incidencias.builder()
                .id(1L)
                .titulo("PC no enciende")
                .descripcion("No responde al pulsar el botón")
                .usuarios(Usuarios.builder()
                        .id(3L)
                        .nombre("Carlos")
                        .apellidos("Martín")
                        .email("carlos@helpdesk.com")
                        .passwordHash("hash_bcrypt")
                        .roles(Roles.builder().id(3L).nombre("Empleado").build())
                        .build())
                .categorias(Categorias.builder().id(1L).nombre("Hardware").build())
                .prioridades(Prioridades.builder().id(3L).nombre("Alta").build())
                .estados(Estados.builder().id(1L).nombre("Abierta").build())
                .build();
        historial = new Historiales("Incidencia creada", tecnico, incidencia);
    }

    @Test
    void listarPorIncidenciaDevuelveElHistorial() {
        when(incidenciasRepositorio.existsById(1L)).thenReturn(true);
        when(historialesRepositorio.findByIncidencias_Id(1L))
                .thenReturn(List.of(historial));

        List<HistorialResponseDTO> resultado =
                historialServicio.listarPorIncidencia(1L);

        assertEquals(1, resultado.size());
        assertEquals("Incidencia creada", resultado.get(0).getAccion());
        assertEquals("Laura García", resultado.get(0).getUsuarioNombre());
        assertEquals(1L, resultado.get(0).getIncidenciaId());
    }

    @Test
    void listarPorIncidenciaInexistenteLanzaExcepcion() {
        when(incidenciasRepositorio.existsById(99L)).thenReturn(false);

        assertThrows(RecursoNoEncontradoException.class,
                () -> historialServicio.listarPorIncidencia(99L));
    }

    @Test
    void listarPorIncidenciaSinIdLanzaExcepcion() {
        assertThrows(SolicitudIncorrectaException.class,
                () -> historialServicio.listarPorIncidencia(null));
    }

    @Test
    void listarPorUsuarioDevuelveSusAcciones() {
        when(historialesRepositorio.findByUsuarios_Id(2L))
                .thenReturn(List.of(historial));

        List<HistorialResponseDTO> resultado =
                historialServicio.listarPorUsuario(2L);

        assertEquals(1, resultado.size());
        assertEquals("Incidencia creada", resultado.get(0).getAccion());
        verify(historialesRepositorio).findByUsuarios_Id(2L);
    }
}
