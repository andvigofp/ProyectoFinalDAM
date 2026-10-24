package com.helpdeskpro.helpdesk_pro.Servicio;

import com.helpdeskpro.helpdesk_pro.DTO.ComentarioRequestDTO;
import com.helpdeskpro.helpdesk_pro.DTO.ComentarioResponseDTO;
import com.helpdeskpro.helpdesk_pro.Entidades.Comentarios;
import com.helpdeskpro.helpdesk_pro.Entidades.Historiales;
import com.helpdeskpro.helpdesk_pro.Entidades.Incidencias;
import com.helpdeskpro.helpdesk_pro.Entidades.Usuarios;
import com.helpdeskpro.helpdesk_pro.Excepciones.RecursoNoEncontradoException;
import com.helpdeskpro.helpdesk_pro.Excepciones.SolicitudIncorrectaException;
import com.helpdeskpro.helpdesk_pro.Repositorio.ComentariosRepositorio;
import com.helpdeskpro.helpdesk_pro.Repositorio.HistorialesRepositorio;
import com.helpdeskpro.helpdesk_pro.Repositorio.IncidenciasRepositorio;
import com.helpdeskpro.helpdesk_pro.Seguridad.SeguridadServicio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ComentarioServicio {

    private final ComentariosRepositorio comentariosRepositorio;
    private final IncidenciasRepositorio incidenciasRepositorio;
    private final HistorialesRepositorio historialesRepositorio;
    private final SeguridadServicio seguridadServicio;

    public ComentarioServicio(
            ComentariosRepositorio comentariosRepositorio,
            IncidenciasRepositorio incidenciasRepositorio,
            HistorialesRepositorio historialesRepositorio,
            SeguridadServicio seguridadServicio) {

        this.comentariosRepositorio = comentariosRepositorio;
        this.incidenciasRepositorio = incidenciasRepositorio;
        this.historialesRepositorio = historialesRepositorio;
        this.seguridadServicio = seguridadServicio;
    }

    // Comentarios de una incidencia (el empleado solo las suyas)
    @Transactional(readOnly = true)
    public List<ComentarioResponseDTO> listarPorIncidencia(Long incidenciaId) {
        Incidencias incidencia = buscarIncidencia(incidenciaId);
        seguridadServicio.verificarAccesoIncidencia(incidencia);

        return comentariosRepositorio.findByIncidencias_Id(incidenciaId)
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    // Comentarios escritos por un usuario (el empleado solo los suyos)
    @Transactional(readOnly = true)
    public List<ComentarioResponseDTO> listarPorUsuario(Long usuarioId) {
        Usuarios actual = seguridadServicio.obtenerUsuarioActual();

        if (seguridadServicio.esEmpleado(actual)
                && !actual.getId().equals(usuarioId)) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "No puedes ver los comentarios de otro usuario"
            );
        }

        return comentariosRepositorio.findByUsuarios_Id(usuarioId)
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    // Añadir un comentario a una incidencia.
    // El autor es siempre el usuario autenticado; un empleado solo
    // puede comentar sus propias incidencias.
    public ComentarioResponseDTO crearComentario(ComentarioRequestDTO dto) {

        if (dto.getMensaje() == null || dto.getMensaje().isBlank()) {
            throw new SolicitudIncorrectaException(
                    "El mensaje no puede estar vacío"
            );
        }

        Incidencias incidencia = buscarIncidencia(dto.getIncidenciaId());
        seguridadServicio.verificarAccesoIncidencia(incidencia);

        Usuarios autor = seguridadServicio.obtenerUsuarioActual();

        Comentarios comentario = Comentarios.builder()
                .mensaje(dto.getMensaje())
                .usuarios(autor)
                .incidencias(incidencia)
                .build();

        Comentarios guardado = comentariosRepositorio.save(comentario);

        historialesRepositorio.save(
                new Historiales("Comentario añadido", autor, incidencia)
        );

        return convertirAResponseDTO(guardado);
    }

    private Incidencias buscarIncidencia(Long id) {
        if (id == null) {
            throw new SolicitudIncorrectaException(
                    "La incidencia es obligatoria"
            );
        }
        return incidenciasRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe la incidencia con ID " + id
                ));
    }

    // Convertir entidad a DTO de respuesta
    private ComentarioResponseDTO convertirAResponseDTO(Comentarios comentario) {

        return ComentarioResponseDTO.builder()
                .id(comentario.getId())
                .mensaje(comentario.getMensaje())
                .fecha(comentario.getFecha())
                .usuarioId(comentario.getUsuarios().getId())
                .usuarioNombre(
                        comentario.getUsuarios().getNombre() + " "
                                + comentario.getUsuarios().getApellidos()
                )
                .incidenciaId(comentario.getIncidencias().getId())
                .build();
    }
}
