package com.helpdeskpro.helpdesk_pro.Servicio;

import com.helpdeskpro.helpdesk_pro.DTO.IncidenciaRequestDTO;
import com.helpdeskpro.helpdesk_pro.DTO.IncidenciaResponseDTO;
import com.helpdeskpro.helpdesk_pro.Entidades.*;
import com.helpdeskpro.helpdesk_pro.Excepciones.RecursoNoEncontradoException;
import com.helpdeskpro.helpdesk_pro.Excepciones.SolicitudIncorrectaException;
import com.helpdeskpro.helpdesk_pro.Repositorio.*;
import com.helpdeskpro.helpdesk_pro.Seguridad.SeguridadServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IncidenciaServicioTest {

    @Mock
    private IncidenciasRepositorio incidenciasRepositorio;
    @Mock
    private UsuariosRepositorio usuariosRepositorio;
    @Mock
    private CategoriasRepositorio categoriasRepositorio;
    @Mock
    private PrioridadesRepositorio prioridadesRepositorio;
    @Mock
    private EstadosRepositorio estadosRepositorio;
    @Mock
    private HistorialesRepositorio historialesRepositorio;
    @Mock
    private SeguridadServicio seguridadServicio;

    @InjectMocks
    private IncidenciaServicio incidenciaServicio;

    private Usuarios empleado;
    private Usuarios tecnico;
    private Categorias categoria;
    private Prioridades prioridad;
    private Estados abierta;
    private Estados enProceso;
    private Estados resuelta;
    private Incidencias incidencia;

    private Usuarios crearUsuario(Long id, String nombre, String apellidos,
                                  String rolNombre, Long rolId) {
        return Usuarios.builder()
                .id(id)
                .nombre(nombre)
                .apellidos(apellidos)
                .email(nombre.toLowerCase() + "@helpdesk.com")
                .passwordHash("hash_bcrypt")
                .roles(Roles.builder().id(rolId).nombre(rolNombre).build())
                .build();
    }

    private Incidencias crearIncidencia(Estados estado) {
        return Incidencias.builder()
                .id(1L)
                .titulo("PC no enciende")
                .descripcion("No responde al pulsar el botón")
                .usuarios(empleado)
                .tecnico(tecnico)
                .categorias(categoria)
                .prioridades(prioridad)
                .estados(estado)
                .build();
    }

    @BeforeEach
    void prepararDatos() {
        empleado = crearUsuario(3L, "Carlos", "Martín", "Empleado", 3L);
        tecnico = crearUsuario(2L, "Laura", "García", "Técnico", 2L);
        categoria = Categorias.builder().id(1L).nombre("Hardware").build();
        prioridad = Prioridades.builder().id(3L).nombre("Alta").build();
        abierta = Estados.builder().id(1L).nombre("Abierta").build();
        enProceso = Estados.builder().id(2L).nombre("En proceso").build();
        resuelta = Estados.builder().id(3L).nombre("Resuelta").build();
        incidencia = crearIncidencia(abierta);
    }

    @Test
    void crearIncidenciaUsaEstadoAbiertaPorDefecto() {
        IncidenciaRequestDTO dto = IncidenciaRequestDTO.builder()
                .titulo("PC no enciende")
                .descripcion("No responde al pulsar el botón")
                .usuarioId(3L)
                .categoriaId(1L)
                .prioridadId(3L)
                .build();

        // El creador es un técnico (no empleado): se respeta el usuarioId
        when(seguridadServicio.obtenerUsuarioActual()).thenReturn(tecnico);
        when(seguridadServicio.esEmpleado(tecnico)).thenReturn(false);
        when(usuariosRepositorio.findById(3L))
                .thenReturn(Optional.of(empleado));
        when(categoriasRepositorio.findById(1L))
                .thenReturn(Optional.of(categoria));
        when(prioridadesRepositorio.findById(3L))
                .thenReturn(Optional.of(prioridad));
        when(estadosRepositorio.findByNombreIgnoreCase("Abierta"))
                .thenReturn(Optional.of(abierta));
        when(incidenciasRepositorio.save(any(Incidencias.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        IncidenciaResponseDTO respuesta =
                incidenciaServicio.crearIncidencia(dto);

        assertEquals("PC no enciende", respuesta.getTitulo());
        assertEquals("Abierta", respuesta.getEstadoNombre());
        assertNull(respuesta.getSolucion());
        verify(historialesRepositorio, times(1))
                .save(any(Historiales.class));
    }

    @Test
    void crearIncidenciaConTituloVacioLanzaExcepcion() {
        IncidenciaRequestDTO dto = IncidenciaRequestDTO.builder()
                .titulo("   ")
                .descripcion("No responde al pulsar el botón")
                .usuarioId(3L)
                .categoriaId(1L)
                .prioridadId(3L)
                .build();

        assertThrows(SolicitudIncorrectaException.class,
                () -> incidenciaServicio.crearIncidencia(dto));

        verify(incidenciasRepositorio, never()).save(any());
    }

    @Test
    void registrarSolucionGuardaSolucionYMarcaResuelta() {
        when(incidenciasRepositorio.findById(1L))
                .thenReturn(Optional.of(incidencia));
        when(estadosRepositorio.findByNombreIgnoreCase("Resuelta"))
                .thenReturn(Optional.of(resuelta));
        when(seguridadServicio.obtenerUsuarioActual()).thenReturn(tecnico);
        when(incidenciasRepositorio.save(any(Incidencias.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        IncidenciaResponseDTO respuesta =
                incidenciaServicio.registrarSolucion(
                        1L, "Se ha sustituido la fuente de alimentación");

        assertEquals("Se ha sustituido la fuente de alimentación",
                respuesta.getSolucion());
        assertEquals("Resuelta", respuesta.getEstadoNombre());
        verify(historialesRepositorio, times(1))
                .save(any(Historiales.class));
    }

    @Test
    void registrarSolucionVaciaLanzaExcepcion() {
        assertThrows(SolicitudIncorrectaException.class,
                () -> incidenciaServicio.registrarSolucion(1L, "  "));
    }

    @Test
    void asignarTecnicoInexistenteLanzaExcepcion() {
        when(incidenciasRepositorio.findById(1L))
                .thenReturn(Optional.of(incidencia));
        when(usuariosRepositorio.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> incidenciaServicio.asignarTecnico(1L, 99L));
    }

    @Test
    void reabrirIncidenciaPoneEstadoEnProceso() {
        incidencia.setEstados(resuelta);

        when(incidenciasRepositorio.findById(1L))
                .thenReturn(Optional.of(incidencia));
        when(estadosRepositorio.findByNombreIgnoreCase("En proceso"))
                .thenReturn(Optional.of(enProceso));
        when(seguridadServicio.obtenerUsuarioActual()).thenReturn(empleado);
        when(incidenciasRepositorio.save(any(Incidencias.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        IncidenciaResponseDTO respuesta =
                incidenciaServicio.reabrirIncidencia(1L);

        assertEquals("En proceso", respuesta.getEstadoNombre());
        verify(historialesRepositorio, times(1))
                .save(any(Historiales.class));
    }

    @Test
    void cambiarEstadoActualizaYRegistraHistorial() {
        when(incidenciasRepositorio.findById(1L))
                .thenReturn(Optional.of(incidencia));
        when(estadosRepositorio.findById(2L))
                .thenReturn(Optional.of(enProceso));
        when(seguridadServicio.obtenerUsuarioActual()).thenReturn(tecnico);
        when(incidenciasRepositorio.save(any(Incidencias.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        IncidenciaResponseDTO respuesta =
                incidenciaServicio.cambiarEstado(1L, 2L);

        assertEquals("En proceso", respuesta.getEstadoNombre());
        verify(historialesRepositorio, times(1))
                .save(any(Historiales.class));
    }

    @Test
    void actualizarIncidenciaModificaElTitulo() {
        IncidenciaRequestDTO dto = IncidenciaRequestDTO.builder()
                .titulo("PC sigue sin encender")
                .build();

        when(incidenciasRepositorio.findById(1L))
                .thenReturn(Optional.of(incidencia));
        when(incidenciasRepositorio.save(any(Incidencias.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        IncidenciaResponseDTO respuesta =
                incidenciaServicio.actualizarIncidencia(1L, dto);

        assertEquals("PC sigue sin encender", respuesta.getTitulo());
        assertNotNull(respuesta.getFechaActualizacion());
    }

    @Test
    void empleadoSoloVeSusPropiasIncidencias() {
        when(seguridadServicio.obtenerUsuarioActual()).thenReturn(empleado);
        when(seguridadServicio.esEmpleado(empleado)).thenReturn(true);
        when(incidenciasRepositorio.findByUsuarios_Id(3L))
                .thenReturn(List.of(incidencia));

        List<IncidenciaResponseDTO> resultado =
                incidenciaServicio.listarPorUsuario(3L);

        assertEquals(1, resultado.size());
        assertEquals(3L, resultado.get(0).getUsuarioId());
    }

    @Test
    void empleadoNoPuedeVerIncidenciasDeOtro() {
        when(seguridadServicio.obtenerUsuarioActual()).thenReturn(empleado);
        when(seguridadServicio.esEmpleado(empleado)).thenReturn(true);

        assertThrows(AccessDeniedException.class,
                () -> incidenciaServicio.listarPorUsuario(4L));
    }
}
