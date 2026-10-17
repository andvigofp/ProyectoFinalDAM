package com.helpdeskpro.helpdesk_pro.Controlador;

import com.helpdeskpro.helpdesk_pro.DTO.ComentarioResponseDTO;
import com.helpdeskpro.helpdesk_pro.Servicio.ComentarioServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Pruebas de caja negra sobre la API de comentarios
@SpringBootTest
@AutoConfigureMockMvc
class ComentarioControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ComentarioServicio comentarioServicio;

    @Test
    void empleadoListaComentariosDeSuIncidencia() throws Exception {
        when(comentarioServicio.listarPorIncidencia(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/comentarios/incidencia/1")
                        .with(user("carlos").roles("EMPLEADO")))
                .andExpect(status().isOk());
    }

    @Test
    void sinUsuarioAutenticadoDevuelve401() throws Exception {
        mockMvc.perform(get("/api/comentarios/incidencia/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void crearComentarioDevuelve201() throws Exception {
        when(comentarioServicio.crearComentario(any()))
                .thenReturn(ComentarioResponseDTO.builder()
                        .id(1L)
                        .mensaje("Estoy revisando la incidencia.")
                        .fecha(LocalDateTime.now())
                        .usuarioId(2L)
                        .usuarioNombre("Laura García")
                        .incidenciaId(1L)
                        .build());

        mockMvc.perform(post("/api/comentarios")
                        .with(user("carlos").roles("EMPLEADO"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"mensaje\":\"Estoy revisando la incidencia.\","
                                + "\"usuarioId\":2,\"incidenciaId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje")
                        .value("Estoy revisando la incidencia."));
    }
}
