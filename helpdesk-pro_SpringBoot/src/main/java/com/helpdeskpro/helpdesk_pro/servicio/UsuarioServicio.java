package com.helpdeskpro.helpdesk_pro.servicio;

import com.helpdeskpro.helpdesk_pro.dto.UsuarioRequestDTO;
import com.helpdeskpro.helpdesk_pro.dto.UsuarioResponseDTO;
import com.helpdeskpro.helpdesk_pro.entidades.Departamentos;
import com.helpdeskpro.helpdesk_pro.entidades.Roles;
import com.helpdeskpro.helpdesk_pro.entidades.Usuarios;
import com.helpdeskpro.helpdesk_pro.excepciones.RecursoDuplicadoException;
import com.helpdeskpro.helpdesk_pro.excepciones.RecursoNoEncontradoException;
import com.helpdeskpro.helpdesk_pro.repositorio.DepartamentosRepositorio;
import com.helpdeskpro.helpdesk_pro.repositorio.RolesRepositorio;
import com.helpdeskpro.helpdesk_pro.repositorio.UsuariosRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class UsuarioServicio {

    private final UsuariosRepositorio usuariosRepositorio;
    private final RolesRepositorio rolesRepositorio;
    private final DepartamentosRepositorio departamentosRepositorio;
    private final PasswordEncoder passwordEncoder;


    // Obtener todos los usuarios
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarUsuarios(String busqueda) {
        return usuariosRepositorio.buscarOrdenados(escaparBusqueda(busqueda))
                .stream()
                .map(this::convertirAResponseDTO)
                .toList();
    }

    // Obtener un usuario por ID
    @Transactional(readOnly = true)
    public UsuarioResponseDTO obtenerUsuarioPorId(Long id) {
        Usuarios usuario = buscarUsuario(id);
        return convertirAResponseDTO(usuario);
    }

    // Registrar un usuario
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto) {

        if (usuariosRepositorio.existsByEmailIgnoreCase(dto.getEmail())) {
            throw new RecursoDuplicadoException(
                    "El correo electrónico ya está registrado"
            );
        }

        Roles rol = rolesRepositorio.findById(dto.getRolId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el rol indicado"
                ));

        Departamentos departamento = null;

        if (dto.getDepartamentoId() != null) {
            departamento = departamentosRepositorio
                    .findById(dto.getDepartamentoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe el departamento indicado"
                    ));
        }

        Usuarios usuario = Usuarios.builder()
                .nombre(dto.getNombre())
                .apellidos(dto.getApellidos())
                .email(dto.getEmail())
                .passwordHash(passwordEncoder.encode(dto.getPassword()))
                .telefono(dto.getTelefono())
                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                .roles(rol)
                .departamentos(departamento)
                .build();

        Usuarios guardado = usuariosRepositorio.save(usuario);

        return convertirAResponseDTO(guardado);
    }

    // Actualizar un usuario
    public UsuarioResponseDTO actualizarUsuario(
            Long id,
            UsuarioRequestDTO dto) {

        Usuarios usuario = buscarUsuario(id);

        usuariosRepositorio.findByEmailIgnoreCase(dto.getEmail())
                .filter(existente -> !existente.getId().equals(id))
                .ifPresent(existente -> {
                    throw new RecursoDuplicadoException(
                            "El correo electrónico ya pertenece a otro usuario"
                    );
                });

        Roles rol = rolesRepositorio.findById(dto.getRolId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el rol indicado"
                ));

        Departamentos departamento = null;

        if (dto.getDepartamentoId() != null) {
            departamento = departamentosRepositorio
                    .findById(dto.getDepartamentoId())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No existe el departamento indicado"
                    ));
        }

        usuario.setNombre(dto.getNombre());
        usuario.setApellidos(dto.getApellidos());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setRoles(rol);
        usuario.setDepartamentos(departamento);
        if (dto.getActivo() != null) {
            usuario.setActivo(dto.getActivo());
        }

        // Solo cambiar la contraseña si se ha enviado una nueva
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            usuario.setPasswordHash(
                    passwordEncoder.encode(dto.getPassword())
            );
        }

        return convertirAResponseDTO(
                usuariosRepositorio.save(usuario)
        );
    }

    // Desactivar un usuario
    public void desactivarUsuario(Long id) {
        Usuarios usuario = buscarUsuario(id);
        usuario.setActivo(false);
        usuariosRepositorio.save(usuario);
    }

    // Comprobar si existe un usuario
    @Transactional(readOnly = true)
    public boolean existeUsuario(Long id) {
        return usuariosRepositorio.existsById(id);
    }

    // Buscar usuario o lanzar una excepción
    private Usuarios buscarUsuario(Long id) {
        return usuariosRepositorio.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No existe el usuario con ID " + id
                ));
    }

    // Convertir entidad a DTO de respuesta
    private UsuarioResponseDTO convertirAResponseDTO(Usuarios usuario) {

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

    private String escaparBusqueda(String busqueda) {
        if (busqueda == null || busqueda.isBlank()) {
            return "";
        }
        return busqueda.trim()
                .replace("!", "!!")
                .replace("%", "!%")
                .replace("_", "!_");
    }
}