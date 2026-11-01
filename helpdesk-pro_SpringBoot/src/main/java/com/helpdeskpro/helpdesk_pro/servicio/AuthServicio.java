package com.helpdeskpro.helpdesk_pro.servicio;

import com.helpdeskpro.helpdesk_pro.dto.LoginRequestDTO;
import com.helpdeskpro.helpdesk_pro.dto.TokenResponseDTO;
import com.helpdeskpro.helpdesk_pro.dto.UsuarioResponseDTO;
import com.helpdeskpro.helpdesk_pro.entidades.Usuarios;
import com.helpdeskpro.helpdesk_pro.excepciones.SolicitudIncorrectaException;
import com.helpdeskpro.helpdesk_pro.repositorio.UsuariosRepositorio;
import com.helpdeskpro.helpdesk_pro.seguridad.JwtServicio;
import com.helpdeskpro.helpdesk_pro.seguridad.SeguridadServicio;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class AuthServicio {

    private final UsuariosRepositorio usuariosRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final JwtServicio jwtServicio;
    private final SeguridadServicio seguridadServicio;


    // Inicio de sesión: valida credenciales y devuelve un token JWT
    @Transactional(readOnly = true)
    public TokenResponseDTO login(LoginRequestDTO dto) {

        if (dto.getEmail() == null || dto.getEmail().isBlank()
                || dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new SolicitudIncorrectaException(
                    "El email y la contraseña son obligatorios"
            );
        }

        Usuarios usuario = usuariosRepositorio
                .findByEmailIgnoreCase(dto.getEmail())
                .orElseThrow(() -> new SolicitudIncorrectaException(
                        "Credenciales incorrectas"
                ));

        if (!passwordEncoder.matches(
                dto.getPassword(), usuario.getPasswordHash())) {
            throw new SolicitudIncorrectaException(
                    "Credenciales incorrectas"
            );
        }

        if (!usuario.getActivo()) {
            throw new SolicitudIncorrectaException(
                    "El usuario está desactivado"
            );
        }

        return TokenResponseDTO.builder()
                .token(jwtServicio.generarToken(usuario))
                .tipo("Bearer")
                .usuario(convertirAUsuarioResponseDTO(usuario))
                .build();
    }

    // Datos del usuario autenticado (para /api/auth/me)
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerUsuarioActual() {
        return convertirAUsuarioResponseDTO(
                seguridadServicio.obtenerUsuarioActual()
        );
    }

    private UsuarioResponseDTO convertirAUsuarioResponseDTO(Usuarios usuario) {

        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellidos(usuario.getApellidos())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .activo(usuario.getActivo())
                .rolId(usuario.getRoles().getId())
                .rolNombre(usuario.getRoles().getNombre())
                .departamentoId(
                        usuario.getDepartamentos() != null
                                ? usuario.getDepartamentos().getId()
                                : null
                )
                .departamentoNombre(
                        usuario.getDepartamentos() != null
                                ? usuario.getDepartamentos().getNombre()
                                : null
                )
                .build();
    }
}
