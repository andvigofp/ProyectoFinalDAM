package com.helpdeskpro.helpdesk_pro.servicio;

import com.helpdeskpro.helpdesk_pro.dto.HistorialResponseDTO;
import com.helpdeskpro.helpdesk_pro.entidades.Historiales;
import com.helpdeskpro.helpdesk_pro.excepciones.RecursoNoEncontradoException;
import com.helpdeskpro.helpdesk_pro.excepciones.SolicitudIncorrectaException;
import com.helpdeskpro.helpdesk_pro.repositorio.HistorialesRepositorio;
import com.helpdeskpro.helpdesk_pro.repositorio.IncidenciasRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class HistorialServicio {

    private final HistorialesRepositorio historialesRepositorio;
    private final IncidenciasRepositorio incidenciasRepositorio;


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
