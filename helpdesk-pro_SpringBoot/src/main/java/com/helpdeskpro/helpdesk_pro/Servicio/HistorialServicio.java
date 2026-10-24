package com.helpdeskpro.helpdesk_pro.Servicio;

import com.helpdeskpro.helpdesk_pro.DTO.HistorialResponseDTO;
import com.helpdeskpro.helpdesk_pro.Entidades.Historiales;
import com.helpdeskpro.helpdesk_pro.Excepciones.RecursoNoEncontradoException;
import com.helpdeskpro.helpdesk_pro.Excepciones.SolicitudIncorrectaException;
import com.helpdeskpro.helpdesk_pro.Repositorio.HistorialesRepositorio;
import com.helpdeskpro.helpdesk_pro.Repositorio.IncidenciasRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class HistorialServicio {

    private final HistorialesRepositorio historialesRepositorio;
    private final IncidenciasRepositorio incidenciasRepositorio;

    public HistorialServicio(
            HistorialesRepositorio historialesRepositorio,
            IncidenciasRepositorio incidenciasRepositorio) {

        this.historialesRepositorio = historialesRepositorio;
        this.incidenciasRepositorio = incidenciasRepositorio;
    }

    // Historial completo de una incidencia
    @Transactional(readOnly = true)
    public List<HistorialResponseDTO> listarPorIncidencia(Long incidenciaId) {

        if (incidenciaId == null) {
            throw new SolicitudIncorrectaException(
                    "La incidencia es obligatoria"
            );
        }

        if (!incidenciasRepositorio.existsById(incidenciaId)) {
            throw new RecursoNoEncontradoException(
                    "No existe la incidencia con ID " + incidenciaId
            );
        }

        return historialesRepositorio.findByIncidencias_Id(incidenciaId)
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    // Acciones registradas por un usuario
    @Transactional(readOnly = true)
    public List<HistorialResponseDTO> listarPorUsuario(Long usuarioId) {
        return historialesRepositorio.findByUsuarios_Id(usuarioId)
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    // Convertir entidad a DTO de respuesta
    private HistorialResponseDTO convertirAResponseDTO(Historiales historial) {

        return HistorialResponseDTO.builder()
                .id(historial.getId())
                .accion(historial.getAccion())
                .fecha(historial.getFecha())
                .usuarioId(historial.getUsuarios().getId())
                .usuarioNombre(
                        historial.getUsuarios().getNombre() + " "
                                + historial.getUsuarios().getApellidos()
                )
                .incidenciaId(historial.getIncidencias().getId())
                .build();
    }
}
