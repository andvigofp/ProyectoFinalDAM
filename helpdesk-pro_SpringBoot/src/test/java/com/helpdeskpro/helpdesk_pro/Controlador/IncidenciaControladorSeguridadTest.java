package com.helpdeskpro.helpdesk_pro.Controlador;

import com.helpdeskpro.helpdesk_pro.DTO.IncidenciaResponseDTO;
import com.helpdeskpro.helpdesk_pro.Servicio.IncidenciaServicio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Pruebas de caja negra sobre los permisos por rol de la API de
// incidencias: se envían peticiones HTTP reales y solo se comprueban
// los códigos de estado según el rol
@SpringBootTest
@AutoConfigureMockMvc
class IncidenciaControladorSeguridadTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IncidenciaServicio incidenciaServicio;

    @Test
    void tecnicoPuedeListarTodasLasIncidencias() throws Exception {
        when(incidenciaServicio.listarIncidencias())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/incidencias")
                        .with(user("laura").roles("TECNICO")))
                .andExpect(status().isOk());
    }

    @Test
    void administradorPuedeListarTodasLasIncidencias() throws Exception {
        when(incidenciaServicio.listarIncidencias())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/incidencias")
                        .with(user("andres").roles("ADMINISTRADOR")))
                .andExpect(status().isOk());
    }

    @Test
    void empleadoNoPuedeListarTodasLasIncidencias() throws Exception {
        mockMvc.perform(get("/api/incidencias")
                        .with(user("carlos").roles("EMPLEADO")))
                .andExpect(status().isForbidden());
    }

    @Test
    void sinUsuarioAutenticadoDevuelve401() throws Exception {
        mockMvc.perform(get("/api/incidencias"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void empleadoPuedeVerSusPropiasIncidencias() throws Exception {
        when(incidenciaServicio.listarPorUsuario(3L))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/incidencias/usuario/3")
                        .with(user("carlos").roles("EMPLEADO")))
                .andExpect(status().isOk());
    }

    @Test
    void tecnicoPuedeRegistrarSolucion() throws Exception {
        when(incidenciaServicio.registrarSolucion(eq(1L), anyString()))
                .thenReturn(IncidenciaResponseDTO.builder().id(1L).build());

        mockMvc.perform(put("/api/incidencias/1/solucion")
                        .with(user("laura").roles("TECNICO"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"solucion\":\"fuente sustituida\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void empleadoNoPuedeRegistrarSolucion() throws Exception {
        mockMvc.perform(put("/api/incidencias/1/solucion")
                        .with(user("carlos").roles("EMPLEADO"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"solucion\":\"intento\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void empleadoPuedeCrearIncidencia() throws Exception {
        mockMvc.perform(post("/api/incidencias")
                        .with(user("carlos").roles("EMPLEADO"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"PC no enciende\","
                                + "\"descripcion\":\"No responde\","
                                + "\"categoriaId\":1,\"prioridadId\":3}"))
                .andExpect(status().isCreated());
    }
}
