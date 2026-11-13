package com.helpdeskpro.helpdesk_pro.controlador;

import com.helpdeskpro.helpdesk_pro.dto.MensajeRespuestaDTO;
import com.helpdeskpro.helpdesk_pro.dto.RestablecerContrasenaRequestDTO;
import com.helpdeskpro.helpdesk_pro.dto.SolicitudRecuperacionContrasenaRequestDTO;
import com.helpdeskpro.helpdesk_pro.servicio.RecuperacionContrasenaServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class RecuperacionContrasenaControlador {

    private final RecuperacionContrasenaServicio recuperacionServicio;

    @PostMapping("/recuperar-contrasena")
    public MensajeRespuestaDTO solicitarRecuperacion(
            @RequestBody SolicitudRecuperacionContrasenaRequestDTO dto) {
        recuperacionServicio.solicitarRecuperacion(dto.getEmail());
        return new MensajeRespuestaDTO(
                "Si el correo está registrado, recibirás instrucciones para recuperar la contraseña."
        );
    }

    @PostMapping("/restablecer-contrasena")
    public ResponseEntity<MensajeRespuestaDTO> restablecerContrasena(
            @RequestBody RestablecerContrasenaRequestDTO dto) {
        boolean restablecida = recuperacionServicio.restablecerContrasena(dto);
        if (!restablecida) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MensajeRespuestaDTO(
                            "El código no es válido, ha caducado o se ha agotado el número de intentos."
                    ));
        }
        return ResponseEntity.ok(new MensajeRespuestaDTO(
                "La contraseña se ha actualizado correctamente."
        ));
    }
}
