package com.helpdeskpro.helpdesk_pro.Controlador;

import com.helpdeskpro.helpdesk_pro.DTO.HistorialResponseDTO;
import com.helpdeskpro.helpdesk_pro.Servicio.HistorialServicio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistorialControlador {

    private final HistorialServicio historialServicio;

    public HistorialControlador(HistorialServicio historialServicio) {
        this.historialServicio = historialServicio;
    }

    // GET /api/historial/incidencia/{incidenciaId}
    @GetMapping("/incidencia/{incidenciaId}")
    public List<HistorialResponseDTO> listarPorIncidencia(
            @PathVariable Long incidenciaId) {

        return historialServicio.listarPorIncidencia(incidenciaId);
    }

    // GET /api/historial/usuario/{usuarioId}
    @GetMapping("/usuario/{usuarioId}")
    public List<HistorialResponseDTO> listarPorUsuario(
            @PathVariable Long usuarioId) {

        return historialServicio.listarPorUsuario(usuarioId);
    }
}