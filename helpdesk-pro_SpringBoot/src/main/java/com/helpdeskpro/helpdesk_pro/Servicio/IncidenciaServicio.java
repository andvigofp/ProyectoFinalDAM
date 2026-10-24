package com.helpdeskpro.helpdesk_pro.Servicio;

import com.helpdeskpro.helpdesk_pro.DTO.IncidenciaRequestDTO;
import com.helpdeskpro.helpdesk_pro.DTO.IncidenciaResponseDTO;
import com.helpdeskpro.helpdesk_pro.Entidades.*;
import com.helpdeskpro.helpdesk_pro.Excepciones.RecursoNoEncontradoException;
import com.helpdeskpro.helpdesk_pro.Excepciones.SolicitudIncorrectaException;
import com.helpdeskpro.helpdesk_pro.Repositorio.*;
import com.helpdeskpro.helpdesk_pro.Seguridad.SeguridadServicio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class IncidenciaServicio {

    private final IncidenciasRepositorio incidenciasRepositorio;
    private final UsuariosRepositorio usuariosRepositorio;
    private final CategoriasRepositorio categoriasRepositorio;
    private final PrioridadesRepositorio prioridadesRepositorio;
    private final EstadosRepositorio estadosRepositorio;
    private final HistorialesRepositorio historialesRepositorio;
    private final SeguridadServicio seguridadServicio;

    public IncidenciaServicio(
            IncidenciasRepositorio incidenciasRepositorio,
            UsuariosRepositorio usuariosRepositorio,
            CategoriasRepositorio categoriasRepositorio,
            PrioridadesRepositorio prioridadesRepositorio,
            EstadosRepositorio estadosRepositorio,
            HistorialesRepositorio historialesRepositorio,
            SeguridadServicio seguridadServicio) {

        this.incidenciasRepositorio = incidenciasRepositorio;
        this.usuariosRepositorio = usuariosRepositorio;
        this.categoriasRepositorio = categoriasRepositorio;
        this.prioridadesRepositorio = prioridadesRepositorio;
        this.estadosRepositorio = estadosRepositorio;
        this.historialesRepositorio = historialesRepositorio;
        this.seguridadServicio = seguridadServicio;
    }

    // Obtener todas las incidencias.
    // Un empleado solo ve las suyas; técnico y administrador ven todas.
    @Transactional(readOnly = true)
    public List<IncidenciaResponseDTO> listarIncidencias() {
        Usuarios actual = seguridadServicio.obtenerUsuarioActual();

        if (seguridadServicio.esEmpleado(actual)) {
            return listarPorUsuario(actual.getId());
        }

        return incidenciasRepositorio.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    // Obtener una incidencia por ID (el empleado solo las suyas)
    @Transactional(readOnly = true)
    public IncidenciaResponseDTO obtenerIncidenciaPorId(Long id) {
        Incidencias incidencia = buscarIncidencia(id);
        seguridadServicio.verificarAccesoIncidencia(incidencia);
        return convertirAResponseDTO(incidencia);
    }

    // Incidencias creadas por un usuario (el empleado solo las suyas)
    @Transactional(readOnly = true)
    public List<IncidenciaResponseDTO> listarPorUsuario(Long usuarioId) {
        Usuarios actual = seguridadServicio.obtenerUsuarioActual();

        if (seguridadServicio.esEmpleado(actual)
                && !actual.getId().equals(usuarioId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "No puedes ver las incidencias de otro usuario"
            );
        }

        return incidenciasRepositorio.findByUsuarios_Id(usuarioId)
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    // Incidencias asignadas a un técnico
    @Transactional(readOnly = true)
    public List<IncidenciaResponseDTO> listarPorTecnico(Long tecnicoId) {
        return incidenciasRepositorio.findByTecnico_Id(tecnicoId)
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    // Incidencias filtradas por estado
    @Transactional(readOnly = true)
    public List<IncidenciaResponseDTO> listarPorEstado(Long estadoId) {
        return incidenciasRepositorio.findByEstados_Id(estadoId)
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    // Incidencias filtradas por prioridad
    @Transactional(readOnly = true)
    public List<IncidenciaResponseDTO> listarPorPrioridad(Long prioridadId) {
        return incidenciasRepositorio.findByPrioridades_Id(prioridadId)
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    // Incidencias filtradas por categoría
    @Transactional(readOnly = true)
    public List<IncidenciaResponseDTO> listarPorCategoria(Long categoriaId) {
        return incidenciasRepositorio.findByCategorias_Id(categoriaId)
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    // Crear una incidencia nueva.
    // El empleado siempre la crea a su nombre.
    public IncidenciaResponseDTO crearIncidencia(IncidenciaRequestDTO dto) {

        validarTexto(dto.getTitulo(), "El título es obligatorio");
        validarTexto(dto.getDescripcion(), "La descripción es obligatoria");

        Usuarios actual = seguridadServicio.obtenerUsuarioActual();

        Usuarios creador = seguridadServicio.esEmpleado(actual)
                ? actual
                : buscarUsuario(dto.getUsuarioId());

        Categorias categoria = buscarCategoria(dto.getCategoriaId());
        Prioridades prioridad = buscarPrioridad(dto.getPrioridadId());

        // Si no se indica estado, la incidencia nace "Abierta"
        Estados estado = dto.getEstadoId() != null
                ? buscarEstado(dto.getEstadoId())
                : buscarEstadoPorNombre("Abierta");

        Usuarios tecnico = null;
        if (dto.getTecnicoId() != null) {
            tecnico = buscarUsuario(dto.getTecnicoId());
        }

        Incidencias incidencia = Incidencias.builder()
                .titulo(dto.getTitulo())
                .descripcion(dto.getDescripcion())
                .usuarios(creador)
                .tecnico(tecnico)
                .categorias(categoria)
                .prioridades(prioridad)
                .estados(estado)
                .build();

        Incidencias guardada = incidenciasRepositorio.save(incidencia);

        registrarHistorial("Incidencia creada", creador, guardada);

        return convertirAResponseDTO(guardada);
    }

    // Actualizar título, descripción, categoría o prioridad
    // (el empleado solo las suyas)
    public IncidenciaResponseDTO actualizarIncidencia(
            Long id, IncidenciaRequestDTO dto) {

        Incidencias incidencia = buscarIncidencia(id);
        seguridadServicio.verificarAccesoIncidencia(incidencia);

        if (dto.getTitulo() != null) {
            validarTexto(dto.getTitulo(), "El título no puede estar vacío");
            incidencia.setTitulo(dto.getTitulo());
        }

        if (dto.getDescripcion() != null) {
            validarTexto(dto.getDescripcion(),
                    "La descripción no puede estar vacía");
            incidencia.setDescripcion(dto.getDescripcion());
        }

        if (dto.getCategoriaId() != null) {
            incidencia.setCategorias(buscarCategoria(dto.getCategoriaId()));
        }

        if (dto.getPrioridadId() != null) {
            incidencia.setPrioridades(buscarPrioridad(dto.getPrioridadId()));
        }

        incidencia.setFechaActualizacion(LocalDateTime.now());

        return convertirAResponseDTO(
                incidenciasRepositorio.save(incidencia)
        );
    }

    // Asignar un técnico a una incidencia (técnico/administrador)
    public IncidenciaResponseDTO asignarTecnico(Long incidenciaId, Long tecnicoId) {

        Incidencias incidencia = buscarIncidencia(incidenciaId);
        Usuarios tecnico = buscarUsuario(tecnicoId);

        incidencia.setTecnico(tecnico);
        incidencia.setFechaActualizacion(LocalDateTime.now());

        registrarHistorial(
                "Incidencia asignada a "
                        + tecnico.getNombre() + " " + tecnico.getApellidos(),
                seguridadServicio.obtenerUsuarioActual(), incidencia
        );

        return convertirAResponseDTO(
                incidenciasRepositorio.save(incidencia)
        );
    }

    // Cambiar el estado de una incidencia (técnico/administrador)
    public IncidenciaResponseDTO cambiarEstado(Long incidenciaId, Long estadoId) {

        Incidencias incidencia = buscarIncidencia(incidenciaId);
        Estados estado = buscarEstado(estadoId);

        incidencia.setEstados(estado);
        incidencia.setFechaActualizacion(LocalDateTime.now());

        registrarHistorial(
                "Estado actualizado a " + estado.getNombre(),
                seguridadServicio.obtenerUsuarioActual(), incidencia
        );

        return convertirAResponseDTO(
                incidenciasRepositorio.save(incidencia)
        );
    }

    // Registrar la solución y marcar la incidencia como resuelta
    public IncidenciaResponseDTO registrarSolucion(
            Long incidenciaId, String solucion) {

        validarTexto(solucion, "La solución no puede estar vacía");

        Incidencias incidencia = buscarIncidencia(incidenciaId);

        incidencia.setSolucion(solucion);
        incidencia.setEstados(buscarEstadoPorNombre("Resuelta"));
        incidencia.setFechaActualizacion(LocalDateTime.now());

        registrarHistorial(
                "Solución registrada",
                seguridadServicio.obtenerUsuarioActual(), incidencia
        );

        return convertirAResponseDTO(
                incidenciasRepositorio.save(incidencia)
        );
    }

    // Reabrir una incidencia resuelta (la confirma el empleado propietario)
    public IncidenciaResponseDTO reabrirIncidencia(Long id) {

        Incidencias incidencia = buscarIncidencia(id);
        seguridadServicio.verificarAccesoIncidencia(incidencia);

        incidencia.setEstados(buscarEstadoPorNombre("En proceso"));
        incidencia.setFechaActualizacion(LocalDateTime.now());

        registrarHistorial(
                "Incidencia reabierta",
                seguridadServicio.obtenerUsuarioActual(), incidencia
        );

        return convertirAResponseDTO(
                incidenciasRepositorio.save(incidencia)
        );
    }

    // Buscar incidencia o lanzar una excepción
    private Incidencias buscarIncidencia(Long id) {
        return incidenciasRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la incidencia con ID " + id
                ));
    }

    private Usuarios buscarUsuario(Long id) {
        if (id == null) {
            throw new SolicitudIncorrectaException(
                    "El identificador de usuario es obligatorio"
            );
        }
        return usuariosRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el usuario con ID " + id
                ));
    }

    private Categorias buscarCategoria(Long id) {
        if (id == null) {
            throw new SolicitudIncorrectaException(
                    "La categoría es obligatoria"
            );
        }
        return categoriasRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la categoría con ID " + id
                ));
    }

    private Prioridades buscarPrioridad(Long id) {
        if (id == null) {
            throw new SolicitudIncorrectaException(
                    "La prioridad es obligatoria"
            );
        }
        return prioridadesRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la prioridad con ID " + id
                ));
    }

    private Estados buscarEstado(Long id) {
        return estadosRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el estado con ID " + id
                ));
    }

    private Estados buscarEstadoPorNombre(String nombre) {
        return estadosRepositorio.findByNombreIgnoreCase(nombre)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el estado '" + nombre + "'"
                ));
    }

    private void validarTexto(String texto, String mensaje) {
        if (texto == null || texto.isBlank()) {
            throw new SolicitudIncorrectaException(mensaje);
        }
    }

    // Guardar una acción en el historial de la incidencia
    private void registrarHistorial(
            String accion, Usuarios usuario, Incidencias incidencia) {

        historialesRepositorio.save(
                new Historiales(accion, usuario, incidencia)
        );
    }

    // Convertir entidad a DTO de respuesta
    private IncidenciaResponseDTO convertirAResponseDTO(Incidencias incidencia) {

        Usuarios tecnico = incidencia.getTecnico();

        return IncidenciaResponseDTO.builder()
                .id(incidencia.getId())
                .titulo(incidencia.getTitulo())
                .descripcion(incidencia.getDescripcion())
                .solucion(incidencia.getSolucion())
                .fechaCreacion(incidencia.getFechaCreacion())
                .fechaActualizacion(incidencia.getFechaActualizacion())
                .usuarioId(incidencia.getUsuarios().getId())
                .usuarioNombre(
                        incidencia.getUsuarios().getNombre() + " "
                                + incidencia.getUsuarios().getApellidos()
                )
                .tecnicoId(tecnico != null ? tecnico.getId() : null)
                .tecnicoNombre(
                        tecnico != null
                                ? tecnico.getNombre() + " " + tecnico.getApellidos()
                                : null
                )
                .categoriaId(incidencia.getCategorias().getId())
                .categoriaNombre(incidencia.getCategorias().getNombre())
                .prioridadId(incidencia.getPrioridades().getId())
                .prioridadNombre(incidencia.getPrioridades().getNombre())
                .estadoId(incidencia.getEstados().getId())
                .estadoNombre(incidencia.getEstados().getNombre())
                .build();
    }
}
