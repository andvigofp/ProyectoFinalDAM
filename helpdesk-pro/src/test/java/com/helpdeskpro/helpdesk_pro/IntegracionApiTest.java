package com.helpdeskpro.helpdesk_pro;

import com.helpdeskpro.helpdesk_pro.DTO.LoginRequestDTO;
import com.helpdeskpro.helpdesk_pro.DTO.TokenResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

// Prueba de integración de extremo a extremo: arranca la aplicación
// completa con base de datos real y consume la API como lo haría
// el cliente (Android): login -> token -> peticiones autorizadas
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class IntegracionApiTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String loginYObtenerToken(String email) {
        LoginRequestDTO credenciales = LoginRequestDTO.builder()
                .email(email)
                .password("Helpdesk123!")
                .build();

        ResponseEntity<TokenResponseDTO> respuesta =
                restTemplate.postForEntity(
                        "/api/auth/login", credenciales,
                        TokenResponseDTO.class);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertNotNull(respuesta.getBody());
        assertNotNull(respuesta.getBody().getToken());
        return respuesta.getBody().getToken();
    }

    private HttpEntity<Void> peticionConToken(String token) {
        HttpHeaders cabeceras = new HttpHeaders();
        cabeceras.setBearerAuth(token);
        return new HttpEntity<>(cabeceras);
    }

    @Test
    void loginDevuelveTokenConRol() {
        String token = loginYObtenerToken("laura@helpdesk.com");
        assertFalse(token.isBlank());
    }

    @Test
    void accesoSinTokenDevuelve401() {
        ResponseEntity<String> respuesta =
                restTemplate.getForEntity("/api/incidencias", String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, respuesta.getStatusCode());
    }

    @Test
    void tecnicoAutenticadoListaTodasLasIncidencias() {
        String token = loginYObtenerToken("laura@helpdesk.com");

        ResponseEntity<String> respuesta = restTemplate.exchange(
                "/api/incidencias", HttpMethod.GET,
                peticionConToken(token), String.class);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertTrue(respuesta.getBody() != null
                && respuesta.getBody().startsWith("["));
    }

    @Test
    void empleadoNoPuedeListarTodasLasIncidencias() {
        String token = loginYObtenerToken("carlos@helpdesk.com");

        ResponseEntity<String> respuesta = restTemplate.exchange(
                "/api/incidencias", HttpMethod.GET,
                peticionConToken(token), String.class);

        assertEquals(HttpStatus.FORBIDDEN, respuesta.getStatusCode());
    }

    @Test
    void empleadoVeSusPropiasIncidencias() {
        String token = loginYObtenerToken("carlos@helpdesk.com");

        ResponseEntity<String> respuesta = restTemplate.exchange(
                "/api/incidencias/usuario/3", HttpMethod.GET,
                peticionConToken(token), String.class);

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
    }
}
