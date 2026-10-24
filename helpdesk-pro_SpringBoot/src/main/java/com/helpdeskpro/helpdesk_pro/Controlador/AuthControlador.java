package com.helpdeskpro.helpdesk_pro.Controlador;

import com.helpdeskpro.helpdesk_pro.DTO.LoginRequestDTO;
import com.helpdeskpro.helpdesk_pro.DTO.TokenResponseDTO;
import com.helpdeskpro.helpdesk_pro.DTO.UsuarioResponseDTO;
import com.helpdeskpro.helpdesk_pro.Servicio.AuthServicio;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthControlador {

    private final AuthServicio authServicio;

    public AuthControlador(AuthServicio authServicio) {
        this.authServicio = authServicio;
    }

    // POST /api/auth/login
    @PostMapping("/login")
    public TokenResponseDTO login(@RequestBody LoginRequestDTO dto) {
        return authServicio.login(dto);
    }

    // GET /api/auth/me -> datos del usuario autenticado
    @GetMapping("/me")
    public UsuarioResponseDTO yo() {
        return authServicio.obtenerUsuarioActual();
    }
}
