package com.helpdeskpro.helpdesk_pro.servicio;

import com.helpdeskpro.helpdesk_pro.dto.RestablecerContrasenaRequestDTO;
import com.helpdeskpro.helpdesk_pro.entidades.RecuperacionContrasena;
import com.helpdeskpro.helpdesk_pro.entidades.Usuarios;
import com.helpdeskpro.helpdesk_pro.excepciones.SolicitudIncorrectaException;
import com.helpdeskpro.helpdesk_pro.repositorio.RecuperacionContrasenaRepositorio;
import com.helpdeskpro.helpdesk_pro.repositorio.UsuariosRepositorio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@Transactional
public class RecuperacionContrasenaServicio {

    private static final int MAX_INTENTOS = 5;
    private static final int MIN_LONGITUD_CONTRASENA = 6;
    private static final SecureRandom GENERADOR = new SecureRandom();

    private final RecuperacionContrasenaRepositorio recuperacionRepositorio;
    private final UsuariosRepositorio usuariosRepositorio;
    private final PasswordEncoder passwordEncoder;
    private final CorreoServicio correoServicio;
    private final String destinatarioPruebas;

    public RecuperacionContrasenaServicio(
            RecuperacionContrasenaRepositorio recuperacionRepositorio,
            UsuariosRepositorio usuariosRepositorio,
            PasswordEncoder passwordEncoder,
            CorreoServicio correoServicio,
            @Value("${app.mail.recuperacion-destinatario:}") String destinatarioPruebas) {
        this.recuperacionRepositorio = recuperacionRepositorio;
        this.usuariosRepositorio = usuariosRepositorio;
        this.passwordEncoder = passwordEncoder;
        this.correoServicio = correoServicio;
        this.destinatarioPruebas = destinatarioPruebas;
    }

    public void solicitarRecuperacion(String email) {
        validarTexto(email, "El email es obligatorio");

        usuariosRepositorio.findByEmailIgnoreCase(email.trim())
                .filter(Usuarios::getActivo)
                .ifPresent(usuario -> {
                    LocalDateTime ahora = LocalDateTime.now();
                    recuperacionRepositorio
                            .findAllByUsuario_IdAndFechaUsoIsNull(usuario.getId())
                            .forEach(recuperacion -> recuperacion.setFechaUso(ahora));

                    String codigo = String.format("%06d", GENERADOR.nextInt(1_000_000));
                    RecuperacionContrasena recuperacion =
                            RecuperacionContrasena.builder()
                                    .usuario(usuario)
                                    .codigoHash(passwordEncoder.encode(codigo))
                                    .fechaExpiracion(ahora.plusMinutes(15))
                                    .intentos(0)
                                    .build();
                    recuperacionRepositorio.save(recuperacion);
                    String destinatario = destinatarioPruebas.isBlank()
                            ? usuario.getEmail()
                            : destinatarioPruebas.trim();
                    correoServicio.enviarCodigoRecuperacion(destinatario, codigo);
                });
    }

    public boolean restablecerContrasena(RestablecerContrasenaRequestDTO dto) {
        validarTexto(dto.getEmail(), "El email es obligatorio");
        validarTexto(dto.getCodigo(), "El código es obligatorio");
        validarTexto(dto.getNuevaContrasena(), "La nueva contraseña es obligatoria");
        if (dto.getNuevaContrasena().length() < MIN_LONGITUD_CONTRASENA) {
            throw new SolicitudIncorrectaException(
                    "La contraseña debe tener al menos 6 caracteres"
            );
        }

        Usuarios usuario = usuariosRepositorio
                .findByEmailIgnoreCase(dto.getEmail().trim())
                .filter(Usuarios::getActivo)
                .orElse(null);
        if (usuario == null) {
            return false;
        }

        RecuperacionContrasena recuperacion = recuperacionRepositorio
                .findFirstByUsuario_IdAndFechaUsoIsNullAndFechaExpiracionAfterOrderByFechaCreacionDesc(
                        usuario.getId(), LocalDateTime.now())
                .orElse(null);
        if (recuperacion == null || recuperacion.getIntentos() >= MAX_INTENTOS) {
            return false;
        }

        if (!passwordEncoder.matches(dto.getCodigo(), recuperacion.getCodigoHash())) {
            recuperacion.setIntentos(recuperacion.getIntentos() + 1);
            if (recuperacion.getIntentos() >= MAX_INTENTOS) {
                recuperacion.setFechaUso(LocalDateTime.now());
            }
            return false;
        }

        usuario.setPasswordHash(passwordEncoder.encode(dto.getNuevaContrasena()));
        usuariosRepositorio.save(usuario);
        recuperacion.setFechaUso(LocalDateTime.now());
        recuperacionRepositorio.save(recuperacion);
        return true;
    }

    private void validarTexto(String valor, String mensaje) {
        if (valor == null || valor.isBlank()) {
            throw new SolicitudIncorrectaException(mensaje);
        }
    }
}
