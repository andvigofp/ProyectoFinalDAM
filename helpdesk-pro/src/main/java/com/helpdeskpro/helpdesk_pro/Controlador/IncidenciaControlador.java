package com.helpdeskpro.helpdesk_pro.Controlador;

import com.helpdeskpro.helpdesk_pro.DTO.IncidenciaRequestDTO;
import com.helpdeskpro.helpdesk_pro.DTO.IncidenciaResponseDTO;
import com.helpdeskpro.helpdesk_pro.Servicio.IncidenciaServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incidencias")
public class IncidenciaControlador {

    private final IncidenciaServicio incidenciaServicio;

    public IncidenciaControlador(IncidenciaServicio incidenciaServicio) {
        this.incidenciaServicio = incidenciaServicio;
    }

    // GET /api/incidencias
    @GetMapping
    public List<IncidenciaResponseDTO> listar() {
        return incidenciaServicio.listarIncidencias();
    }

    // GET /api/incidencias/{id}
    @GetMapping("/{id}")
    public IncidenciaResponseDTO obtenerPorId(@PathVariable Long id) {
        return incidenciaServicio.obtenerIncidenciaPorId(id);
    }

    // GET /api/incidencias/usuario/{usuarioId}
    @GetMapping("/usuario/{usuarioId}")
    public List<IncidenciaResponseDTO> listarPorUsuario(
            @PathVariable Long usuarioId) {

        return incidenciaServicio.listarPorUsuario(usuarioId);
    }

    // GET /api/incidencias/tecnico/{tecnicoId}
    @GetMapping("/tecnico/{tecnicoId}")
    public List<IncidenciaResponseDTO> listarPorTecnico(
            @PathVariable Long tecnicoId) {

        return incidenciaServicio.listarPorTecnico(tecnicoId);
    }

    // GET /api/incidencias/estado/{estadoId}
    @GetMapping("/estado/{estadoId}")
    public List<IncidenciaResponseDTO> listarPorEstado(
            @PathVariable Long estadoId) {

        return incidenciaServicio.listarPorEstado(estadoId);
    }

    // GET /api/incidencias/prioridad/{prioridadId}
    @GetMapping("/prioridad/{prioridadId}")
    public List<IncidenciaResponseDTO> listarPorPrioridad(
            @PathVariable Long prioridadId) {

        return incidenciaServicio.listarPorPrioridad(prioridadId);
    }

    // GET /api/incidencias/categoria/{categoriaId}
    @GetMapping("/categoria/{categoriaId}")
    public List<IncidenciaResponseDTO> listarPorCategoria(
            @PathVariable Long categoriaId) {

        return incidenciaServicio.listarPorCategoria(categoriaId);
    }

    // POST /api/incidencias
    @PostMapping
    public ResponseEntity<IncidenciaResponseDTO> crear(
            @RequestBody IncidenciaRequestDTO dto) {

        IncidenciaResponseDTO creada = incidenciaServicio.crearIncidencia(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // PUT /api/incidencias/{id}
    @PutMapping("/{id}")
    public IncidenciaResponseDTO actualizar(
            @PathVariable Long id,
            @RequestBody IncidenciaRequestDTO dto) {

        return incidenciaServicio.actualizarIncidencia(id, dto);
    }

    // PUT /api/incidencias/{id}/asignar/{tecnicoId}
    @PutMapping("/{id}/asignar/{tecnicoId}")
    public IncidenciaResponseDTO asignarTecnico(
            @PathVariable Long id,
            @PathVariable Long tecnicoId) {

        return incidenciaServicio.asignarTecnico(id, tecnicoId);
    }

    // PUT /api/incidencias/{id}/estado/{estadoId}
    @PutMapping("/{id}/estado/{estadoId}")
    public IncidenciaResponseDTO cambiarEstado(
            @PathVariable Long id,
            @PathVariable Long estadoId) {

        return incidenciaServicio.cambiarEstado(id, estadoId);
    }

    // PUT /api/incidencias/{id}/solucion
    @PutMapping("/{id}/solucion")
    public IncidenciaResponseDTO registrarSolucion(
            @PathVariable Long id,
            @RequestBody IncidenciaRequestDTO dto) {

        return incidenciaServicio.registrarSolucion(id, dto.getSolucion());
    }

    // PUT /api/incidencias/{id}/reabrir
    @PutMapping("/{id}/reabrir")
    public IncidenciaResponseDTO reabrir(@PathVariable Long id) {
        return incidenciaServicio.reabrirIncidencia(id);
    }
}