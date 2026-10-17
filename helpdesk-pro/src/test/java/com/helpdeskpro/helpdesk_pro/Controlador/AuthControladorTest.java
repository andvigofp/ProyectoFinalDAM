package com.helpdeskpro.helpdesk_pro.Controlador;

import com.helpdeskpro.helpdesk_pro.DTO.LoginRequestDTO;
import com.helpdeskpro.helpdesk_pro.DTO.TokenResponseDTO;
import com.helpdeskpro.helpdesk_pro.DTO.UsuarioResponseDTO;
import com.helpdeskpro.helpdesk_pro.Excepciones.SolicitudIncorrectaException;
import com.helpdeskpro.helpdesk_pro.Servicio.AuthServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Pruebas de caja negra sobre la API de autenticación:
// se envían peticiones HTTP reales y solo se comprueban las respuestas
@SpringBootTest
@AutoConfigureMockMvc
class AuthControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthServicio authServicio;

    @Test
    void loginCorrectoDevuelveToken() throws Exception {
        TokenResponseDTO respuesta = TokenResponseDTO.builder()
                .token("token-jwt")
                .tipo("Bearer")
                .usuario(UsuarioResponseDTO.builder()
                        .id(2L)
                        .email("laura@helpdesk.com")
                        .rolNombre("Técnico")
                        .build())
                .build();

        when(authServicio.login(any(LoginRequestDTO.class)))
                .thenReturn(respuesta);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"laura@helpdesk.com\","
                                + "\"password\":\"Helpdesk123!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-jwt"))
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.usuario.rolNombre").value("Técnico"));
    }

    @Test
    void loginConCredencialesIncorrectasDevuelve400() throws Exception {
        when(authServicio.login(any(LoginRequestDTO.class)))
                .thenThrow(new SolicitudIncorrectaException(
                        "Credenciales incorrectas"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"laura@helpdesk.com\","
                                + "\"password\":\"mala\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje")
                        .value("Credenciales incorrectas"));
    }

    @Test
    void meSinTokenDevuelve401() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void meConUsuarioAutenticadoDevuelve200() throws Exception {
        when(authServicio.obtenerUsuarioActual())
                .thenReturn(UsuarioResponseDTO.builder()
                        .id(2L)
                        .email("laura@helpdesk.com")
                        .rolNombre("Técnico")
                        .build());

        mockMvc.perform(get("/api/auth/me")
                        .with(user("laura@helpdesk.com").roles("TECNICO")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email")
                        .value("laura@helpdesk.com"));
    }
}
