package com.helpdeskpro.helpdesk_pro.Controlador;

import com.helpdeskpro.helpdesk_pro.DTO.UsuarioRequestDTO;
import com.helpdeskpro.helpdesk_pro.DTO.UsuarioResponseDTO;
import com.helpdeskpro.helpdesk_pro.Servicio.UsuarioServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;

    public UsuarioControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    // GET /api/usuarios
    @GetMapping
    public List<UsuarioResponseDTO> listar() {
        return usuarioServicio.listarUsuarios();
    }

    // GET /api/usuarios/{id}
    @GetMapping("/{id}")
    public UsuarioResponseDTO obtenerPorId(@PathVariable Long id) {
        return usuarioServicio.obtenerUsuarioPorId(id);
    }

    // POST /api/usuarios
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> crear(
            @RequestBody UsuarioRequestDTO dto) {

        UsuarioResponseDTO creado = usuarioServicio.crearUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // PUT /api/usuarios/{id}
    @PutMapping("/{id}")
    public UsuarioResponseDTO actualizar(
            @PathVariable Long id,
            @RequestBody UsuarioRequestDTO dto) {

        return usuarioServicio.actualizarUsuario(id, dto);
    }

    // DELETE /api/usuarios/{id} (desactiva, no borra físicamente)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        usuarioServicio.desactivarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
