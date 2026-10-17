
package com.helpdeskpro.helpdesk_pro.Excepciones;

import com.helpdeskpro.helpdesk_pro.DTO.ErrorRespuestaDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ManejadorExcepcionesGlobal {

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarNoEncontrado(
            RecursoNoEncontradoException ex,
            HttpServletRequest request) {

        return crearRespuesta(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarDuplicado(
            RecursoDuplicadoException ex,
            HttpServletRequest request) {

        return crearRespuesta(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(SolicitudIncorrectaException.class)
    public ResponseEntity<ErrorRespuestaDTO> manejarSolicitudIncorrecta(
            SolicitudIncorrectaException ex,
            HttpServletRequest request) {

        return crearRespuesta(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    private ResponseEntity<ErrorRespuestaDTO> crearRespuesta(
            HttpStatus estado,
            String mensaje,
            String ruta) {

        ErrorRespuestaDTO respuesta = ErrorRespuestaDTO.builder()
                .fecha(LocalDateTime.now())
                .estado(estado.value())
                .error(estado.getReasonPhrase())
                .mensaje(mensaje)
                .ruta(ruta)
                .build();

        return ResponseEntity.status(estado).body(respuesta);
    }
}
