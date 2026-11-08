package com.helpdeskpro.helpdesk_pro.servicio;

import com.helpdeskpro.helpdesk_pro.dto.LoginRequestDTO;
import com.helpdeskpro.helpdesk_pro.dto.PerfilActualizacionRequestDTO;
import com.helpdeskpro.helpdesk_pro.dto.RegistroRequestDTO;
import com.helpdeskpro.helpdesk_pro.dto.TokenResponseDTO;
import com.helpdeskpro.helpdesk_pro.dto.UsuarioResponseDTO;
import com.helpdeskpro.helpdesk_pro.entidades.Departamentos;
import com.helpdeskpro.helpdesk_pro.entidades.Roles;
import com.helpdeskpro.helpdesk_pro.entidades.Usuarios;
import com.helpdeskpro.helpdesk_pro.excepciones.RecursoDuplicadoException;
import com.helpdeskpro.helpdesk_pro.excepciones.RecursoNoEncontradoException;
import com.helpdeskpro.helpdesk_pro.excepciones.SolicitudIncorrectaException;
import com.helpdeskpro.helpdesk_pro.repositorio.DepartamentosRepositorio;
import com.helpdeskpro.helpdesk_pro.repositorio.RolesRepositorio;
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
    private final RolesRepositorio rolesRepositorio;
    private final DepartamentosRepositorio departamentosRepositorio;
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

    public UsuarioResponseDTO registrar(RegistroRequestDTO dto) {
        validarTexto(dto.getNombre(), "El nombre es obligatorio");
        validarTexto(dto.getApellidos(), "Los apellidos son obligatorios");
        validarTexto(dto.getEmail(), "El email es obligatorio");
        validarTexto(dto.getPassword(), "La contraseña es obligatoria");
        if (dto.getPassword().length() < 6) {
            throw new SolicitudIncorrectaException(
                    "La contraseña debe tener al menos 6 caracteres"
            );
        }
        if (usuariosRepositorio.existsByEmailIgnoreCase(dto.getEmail().trim())) {
            throw new RecursoDuplicadoException(
                    "El correo electrónico ya está registrado"
            );
        }

        Roles rolEmpleado = rolesRepositorio.findByNombreIgnoreCase("Empleado")
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el rol Empleado"
                ));
        Departamentos departamento = dto.getDepartamentoId() == null
                ? null
                : departamentosRepositorio.findById(dto.getDepartamentoId())
                        .orElseThrow(() -> new RecursoNoEncontradoException(
                                "No existe el departamento indicado"
                        ));

        Usuarios usuario = Usuarios.builder()
                .nombre(dto.getNombre().trim())
                .apellidos(dto.getApellidos().trim())
                .email(dto.getEmail().trim())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .telefono(dto.getTelefono())
                .activo(true)
                .roles(rolEmpleado)
                .departamentos(departamento)
                .build();
        return convertirAUsuarioResponseDTO(usuariosRepositorio.save(usuario));
    }

    public UsuarioResponseDTO actualizarPerfil(PerfilActualizacionRequestDTO dto) {
        validarTexto(dto.getNombre(), "El nombre es obligatorio");
        validarTexto(dto.getApellidos(), "Los apellidos son obligatorios");
        validarTexto(dto.getEmail(), "El email es obligatorio");

        Usuarios usuario = seguridadServicio.obtenerUsuarioActual();
        usuariosRepositorio.findByEmailIgnoreCase(dto.getEmail().trim())
                .filter(existente -> !existente.getId().equals(usuario.getId()))
                .ifPresent(existente -> {
                    throw new RecursoDuplicadoException(
                            "El correo electrónico ya pertenece a otro usuario"
                    );
                });

        usuario.setNombre(dto.getNombre().trim());
        usuario.setApellidos(dto.getApellidos().trim());
        usuario.setEmail(dto.getEmail().trim());
        usuario.setTelefono(dto.getTelefono());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            if (dto.getPassword().length() < 6) {
                throw new SolicitudIncorrectaException(
                        "La contraseña debe tener al menos 6 caracteres"
                );
            }
            usuario.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        }
        return convertirAUsuarioResponseDTO(usuariosRepositorio.save(usuario));
    }

    private void validarTexto(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new SolicitudIncorrectaException(mensaje);
        }
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
                .fechaRegistro(usuario.getFechaRegistro())
                .build();
    }
}
