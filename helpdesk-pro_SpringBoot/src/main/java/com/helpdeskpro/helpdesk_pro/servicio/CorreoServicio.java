package com.helpdeskpro.helpdesk_pro.servicio;

import com.helpdeskpro.helpdesk_pro.excepciones.ErrorEnvioCorreoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CorreoServicio {

    private static final Logger LOGGER = LoggerFactory.getLogger(CorreoServicio.class);

    private final JavaMailSender mailSender;
    private final String remitente;

    public CorreoServicio(
            JavaMailSender mailSender,
            @Value("${app.mail.remitente}") String remitente) {
        this.mailSender = mailSender;
        this.remitente = remitente;
    }

    public void enviarCodigoRecuperacion(String destinatario, String codigo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitente);
        mensaje.setTo(destinatario);
        mensaje.setSubject("Recuperación de contraseña - Helpdesk Pro");
        mensaje.setText("""
                Hemos recibido una solicitud para restablecer la contraseña de tu cuenta.

                Tu código de verificación es: %s

                El código caduca en 15 minutos y solo se puede utilizar una vez.
                Si no has solicitado este cambio, puedes ignorar este correo.
                """.formatted(codigo));

        try {
            mailSender.send(mensaje);
        } catch (MailException excepcion) {
            LOGGER.error("Falló el envío SMTP del código de recuperación.", excepcion);
            throw new ErrorEnvioCorreoException(
                    "No se pudo enviar el correo de recuperación.",
                    excepcion
            );
        }
    }
}
