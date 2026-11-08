package com.helpdeskpro.helpdesk_pro.servicio;

import com.helpdeskpro.helpdesk_pro.dto.LoginRequestDTO;
import com.helpdeskpro.helpdesk_pro.dto.RegistroRequestDTO;
import com.helpdeskpro.helpdesk_pro.dto.TokenResponseDTO;
import com.helpdeskpro.helpdesk_pro.entidades.Roles;
import com.helpdeskpro.helpdesk_pro.entidades.Usuarios;
import com.helpdeskpro.helpdesk_pro.excepciones.SolicitudIncorrectaException;
import com.helpdeskpro.helpdesk_pro.repositorio.DepartamentosRepositorio;
import com.helpdeskpro.helpdesk_pro.repositorio.RolesRepositorio;
import com.helpdeskpro.helpdesk_pro.repositorio.UsuariosRepositorio;
import com.helpdeskpro.helpdesk_pro.seguridad.JwtServicio;
import com.helpdeskpro.helpdesk_pro.seguridad.SeguridadServicio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServicioTest {

    @Mock
    private UsuariosRepositorio usuariosRepositorio;
    @Mock
    private RolesRepositorio rolesRepositorio;
    @Mock
    private DepartamentosRepositorio departamentosRepositorio;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtServicio jwtServicio;
    @Mock
    private SeguridadServicio seguridadServicio;

    @InjectMocks
    private AuthServicio authServicio;

    private Usuarios tecnico;

    @BeforeEach
    void prepararDatos() {
        tecnico = Usuarios.builder()
                .id(2L)
                .nombre("Laura")
                .apellidos("García")
                .email("laura@helpdesk.com")
                .passwordHash("hash_bcrypt")
                .activo(true)
                .roles(Roles.builder().id(2L).nombre("Técnico").build())
                .build();
    }

    @Test
    void loginCorrectoDevuelveToken() {
        LoginRequestDTO dto = LoginRequestDTO.builder()
                .email("laura@helpdesk.com")
                .password("Helpdesk123!")
                .build();

        when(usuariosRepositorio.findByEmailIgnoreCase("laura@helpdesk.com"))
                .thenReturn(Optional.of(tecnico));
        when(passwordEncoder.matches("Helpdesk123!", "hash_bcrypt"))
                .thenReturn(true);
        when(jwtServicio.generarToken(tecnico)).thenReturn("token-jwt");

        TokenResponseDTO respuesta = authServicio.login(dto);

        assertEquals("token-jwt", respuesta.getToken());
        assertEquals("Bearer", respuesta.getTipo());
        assertEquals("Técnico", respuesta.getUsuario().getRolNombre());
    }

    @Test
    void loginConContrasenaIncorrectaLanzaExcepcion() {
        LoginRequestDTO dto = LoginRequestDTO.builder()
                .email("laura@helpdesk.com")
                .password("mala")
                .build();

        when(usuariosRepositorio.findByEmailIgnoreCase("laura@helpdesk.com"))
                .thenReturn(Optional.of(tecnico));
        when(passwordEncoder.matches("mala", "hash_bcrypt"))
                .thenReturn(false);

        assertThrows(SolicitudIncorrectaException.class,
                () -> authServicio.login(dto));
    }

    @Test
    void loginConEmailInexistenteLanzaExcepcion() {
        LoginRequestDTO dto = LoginRequestDTO.builder()
                .email("nadie@helpdesk.com")
                .password("Helpdesk123!")
                .build();

        when(usuariosRepositorio.findByEmailIgnoreCase("nadie@helpdesk.com"))
                .thenReturn(Optional.empty());

        assertThrows(SolicitudIncorrectaException.class,
                () -> authServicio.login(dto));
    }

    @Test
    void registroPublicoSiempreCreaUnEmpleado() {
        RegistroRequestDTO dto = new RegistroRequestDTO(
                "Ana", "Pérez", "ana.nueva@helpdesk.com",
                "password123", "600111222", null
        );
        Roles empleado = Roles.builder().id(3L).nombre("Empleado").build();
        when(usuariosRepositorio.existsByEmailIgnoreCase(dto.getEmail()))
                .thenReturn(false);
        when(rolesRepositorio.findByNombreIgnoreCase("Empleado"))
                .thenReturn(Optional.of(empleado));
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("password-hash");
        when(usuariosRepositorio.save(any(Usuarios.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var creado = authServicio.registrar(dto);

        assertEquals("Empleado", creado.getRolNombre());
        assertEquals(dto.getEmail(), creado.getEmail());
        verify(rolesRepositorio).findByNombreIgnoreCase("Empleado");
    }
}
