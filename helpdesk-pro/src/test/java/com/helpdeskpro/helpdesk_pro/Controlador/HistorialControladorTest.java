package com.helpdeskpro.helpdesk_pro.Controlador;

import com.helpdeskpro.helpdesk_pro.DTO.HistorialResponseDTO;
import com.helpdeskpro.helpdesk_pro.Servicio.HistorialServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Pruebas de caja negra sobre la API de historial
@SpringBootTest
@AutoConfigureMockMvc
class HistorialControladorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HistorialServicio historialServicio;

    @Test
    void empleadoConsultaHistorialDeSuIncidencia() throws Exception {
        when(historialServicio.listarPorIncidencia(1L))
                .thenReturn(List.of(
                        HistorialResponseDTO.builder()
                                .id(1L)
                                .accion("Incidencia creada")
                                .fecha(LocalDateTime.now())
                                .usuarioId(3L)
                                .usuarioNombre("Carlos Martín")
                                .incidenciaId(1L)
                                .build()
                ));

        mockMvc.perform(get("/api/historial/incidencia/1")
                        .with(user("carlos").roles("EMPLEADO")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accion")
                        .value("Incidencia creada"));
    }

    @Test
    void sinUsuarioAutenticadoDevuelve401() throws Exception {
        mockMvc.perform(get("/api/historial/incidencia/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void tecnicoConsultaHistorialPorUsuario() throws Exception {
        when(historialServicio.listarPorUsuario(2L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/historial/usuario/2")
                        .with(user("laura").roles("TECNICO")))
                .andExpect(status().isOk());
    }
}
