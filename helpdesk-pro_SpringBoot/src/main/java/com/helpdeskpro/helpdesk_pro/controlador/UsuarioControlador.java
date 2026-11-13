package com.helpdeskpro.helpdesk_pro.controlador;

import com.helpdeskpro.helpdesk_pro.dto.UsuarioRequestDTO;
import com.helpdeskpro.helpdesk_pro.dto.UsuarioResponseDTO;
import com.helpdeskpro.helpdesk_pro.servicio.UsuarioServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@AllArgsConstructor
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;


    // GET /api/usuarios
    @GetMapping
    public List<UsuarioResponseDTO> listar(
            @RequestParam(defaultValue = "") String busqueda) {
        return usuarioServicio.listarUsuarios(busqueda);
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
