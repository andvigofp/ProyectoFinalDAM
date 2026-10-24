package com.helpdeskpro.helpdesk_pro.Servicio;

import com.helpdeskpro.helpdesk_pro.DTO.UsuarioRequestDTO;
import com.helpdeskpro.helpdesk_pro.DTO.UsuarioResponseDTO;
import com.helpdeskpro.helpdesk_pro.Entidades.Departamentos;
import com.helpdeskpro.helpdesk_pro.Entidades.Roles;
import com.helpdeskpro.helpdesk_pro.Entidades.Usuarios;
import com.helpdeskpro.helpdesk_pro.Excepciones.RecursoDuplicadoException;
import com.helpdeskpro.helpdesk_pro.Excepciones.RecursoNoEncontradoException;
import com.helpdeskpro.helpdesk_pro.Repositorio.DepartamentosRepositorio;
import com.helpdeskpro.helpdesk_pro.Repositorio.RolesRepositorio;
import com.helpdeskpro.helpdesk_pro.Repositorio.UsuariosRepositorio;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UsuarioServicio {

    private final UsuariosRepositorio usuariosRepositorio;
    private final RolesRepositorio rolesRepositorio;
    private final DepartamentosRepositorio departamentosRepositorio;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServicio(
            UsuariosRepositorio usuariosRepositorio,
            RolesRepositorio rolesRepositorio,
            DepartamentosRepositorio departamentosRepositorio,
            PasswordEncoder passwordEncoder) {

        this.usuariosRepositorio = usuariosRepositorio;
        this.rolesRepositorio = rolesRepositorio;
        this.departamentosRepositorio = departamentosRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    // Obtener todos los usuarios
    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarUsuarios() {
        return usuariosRepositorio.findAll()
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
                .build();
    }
}