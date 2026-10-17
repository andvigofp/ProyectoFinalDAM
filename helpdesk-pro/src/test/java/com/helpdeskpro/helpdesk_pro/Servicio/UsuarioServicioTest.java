package com.helpdeskpro.helpdesk_pro.Servicio;

import com.helpdeskpro.helpdesk_pro.DTO.UsuarioRequestDTO;
import com.helpdeskpro.helpdesk_pro.Entidades.Roles;
import com.helpdeskpro.helpdesk_pro.Entidades.Usuarios;
import com.helpdeskpro.helpdesk_pro.Excepciones.RecursoDuplicadoException;
import com.helpdeskpro.helpdesk_pro.Excepciones.RecursoNoEncontradoException;
import com.helpdeskpro.helpdesk_pro.Repositorio.DepartamentosRepositorio;
import com.helpdeskpro.helpdesk_pro.Repositorio.RolesRepositorio;
import com.helpdeskpro.helpdesk_pro.Repositorio.UsuariosRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServicioTest {

    @Mock
    private UsuariosRepositorio usuariosRepositorio;
    @Mock
    private RolesRepositorio rolesRepositorio;
    @Mock
    private DepartamentosRepositorio departamentosRepositorio;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServicio usuarioServicio;

    private Roles rolEmpleado;

    @BeforeEach
    void prepararDatos() {
        rolEmpleado = Roles.builder().id(3L).nombre("Empleado").build();
    }

    @Test
    void crearUsuarioConEmailDuplicadoLanzaExcepcion() {
        UsuarioRequestDTO dto = UsuarioRequestDTO.builder()
                .nombre("Marta")
                .apellidos("Ruiz")
                .email("marta@helpdesk.com")
                .password("Helpdesk123!")
                .rolId(3L)
                .build();

        when(usuariosRepositorio.existsByEmailIgnoreCase("marta@helpdesk.com"))
                .thenReturn(true);

        assertThrows(RecursoDuplicadoException.class,
                () -> usuarioServicio.crearUsuario(dto));

        verify(usuariosRepositorio, never()).save(any());
    }

    @Test
    void crearUsuarioConRolInexistenteLanzaExcepcion() {
        UsuarioRequestDTO dto = UsuarioRequestDTO.builder()
                .nombre("Marta")
                .apellidos("Ruiz")
                .email("marta@helpdesk.com")
                .password("Helpdesk123!")
                .rolId(99L)
                .build();

        when(usuariosRepositorio.existsByEmailIgnoreCase(anyString()))
                .thenReturn(false);
        when(rolesRepositorio.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> usuarioServicio.crearUsuario(dto));
    }

    @Test
    void crearUsuarioCodificaLaContrasena() {
        UsuarioRequestDTO dto = UsuarioRequestDTO.builder()
                .nombre("Marta")
                .apellidos("Ruiz")
                .email("marta@helpdesk.com")
                .password("Helpdesk123!")
                .rolId(3L)
                .build();

        when(usuariosRepositorio.existsByEmailIgnoreCase(anyString()))
                .thenReturn(false);
        when(rolesRepositorio.findById(3L))
                .thenReturn(Optional.of(rolEmpleado));
        when(passwordEncoder.encode("Helpdesk123!"))
                .thenReturn("hash_bcrypt");
        when(usuariosRepositorio.save(any(Usuarios.class)))
                .thenAnswer(invocacion -> invocacion.getArgument(0));

        var respuesta = usuarioServicio.crearUsuario(dto);

        assertEquals("hash_bcrypt",
                respuesta != null ? "hash_bcrypt" : null);
        verify(passwordEncoder, times(1)).encode("Helpdesk123!");
    }

    @Test
    void desactivarUsuarioPoneActivoAFalso() {
        Usuarios usuario = Usuarios.builder()
                .id(3L)
                .nombre("Carlos")
                .apellidos("Martín")
                .email("carlos@helpdesk.com")
                .passwordHash("hash")
                .activo(true)
                .roles(rolEmpleado)
                .build();

        when(usuariosRepositorio.findById(3L))
                .thenReturn(Optional.of(usuario));

        usuarioServicio.desactivarUsuario(3L);

        assertFalse(usuario.getActivo());
        verify(usuariosRepositorio, times(1)).save(usuario);
    }

    @Test
    void desactivarUsuarioInexistenteLanzaExcepcion() {
        when(usuariosRepositorio.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class,
                () -> usuarioServicio.desactivarUsuario(99L));
    }
}
