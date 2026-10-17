package com.helpdeskpro.helpdesk_pro.Controlador;

import com.helpdeskpro.helpdesk_pro.DTO.ComentarioRequestDTO;
import com.helpdeskpro.helpdesk_pro.DTO.ComentarioResponseDTO;
import com.helpdeskpro.helpdesk_pro.Servicio.ComentarioServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioControlador {

    private final ComentarioServicio comentarioServicio;

    public ComentarioControlador(ComentarioServicio comentarioServicio) {
        this.comentarioServicio = comentarioServicio;
    }

    // GET /api/comentarios/incidencia/{incidenciaId}
    @GetMapping("/incidencia/{incidenciaId}")
    public List<ComentarioResponseDTO> listarPorIncidencia(
            @PathVariable Long incidenciaId) {

        return comentarioServicio.listarPorIncidencia(incidenciaId);
    }

    // GET /api/comentarios/usuario/{usuarioId}
    @GetMapping("/usuario/{usuarioId}")
    public List<ComentarioResponseDTO> listarPorUsuario(
            @PathVariable Long usuarioId) {

        return comentarioServicio.listarPorUsuario(usuarioId);
    }

    // POST /api/comentarios
    @PostMapping
    public ResponseEntity<ComentarioResponseDTO> crear(
            @RequestBody ComentarioRequestDTO dto) {

        ComentarioResponseDTO creado = comentarioServicio.crearComentario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }
}