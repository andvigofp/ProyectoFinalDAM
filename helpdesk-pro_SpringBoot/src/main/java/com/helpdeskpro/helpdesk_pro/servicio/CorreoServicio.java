package com.helpdeskpro.helpdesk_pro.servicio;

import com.helpdeskpro.helpdesk_pro.excepciones.ErrorEnvioCorreoException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import java.util.List;
import java.util.Map;

@Service
public class CorreoServicio {

    private static final Logger LOGGER = LoggerFactory.getLogger(CorreoServicio.class);

    private final JavaMailSender mailSender;
    private final String remitenteGmail;
    private final String resendApiKey;
    private final String remitenteResend;
    private final RestClient restClient;

    public CorreoServicio(
            JavaMailSender mailSender,
            @Value("${app.mail.remitente:}") String remitenteGmail,
            @Value("${resend.api.key:}") String resendApiKey,
            @Value("${app.mail.resend-remitente:onboarding@resend.dev}") String remitenteResend) {
        this.mailSender = mailSender;
        this.remitenteGmail = remitenteGmail;
        this.resendApiKey = resendApiKey;
        this.remitenteResend = remitenteResend;
        this.restClient = RestClient.create("https://api.resend.com");
    }

    public void enviarCodigoRecuperacion(String destinatario, String codigo) {
        String cuerpoTexto = """
                Hemos recibido una solicitud para restablecer la contraseña de tu cuenta.

                Tu código de verificación es: %s

                El código caduca en 15 minutos y solo se puede utilizar una vez.
                Si no has solicitado este cambio, puedes ignorar este correo.
                """.formatted(codigo);

        // Si hay API Key de Resend (Railway), envía por HTTP API
        if (resendApiKey != null && !resendApiKey.isBlank()) {
            Map<String, Object> body = Map.of(
                    "from", remitenteResend,
                    "to", List.of(destinatario),
                    "subject", "Recuperación de contraseña - Helpdesk Pro",
                    "text", cuerpoTexto
            );

            try {
                restClient.post()
                        .uri("/emails")
                        .header("Authorization", "Bearer " + resendApiKey)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(body)
                        .retrieve()
                        .toBodilessEntity();
            } catch (RestClientException excepcion) {
                LOGGER.error("Falló el envío por Resend del código de recuperación.", excepcion);
                throw new ErrorEnvioCorreoException(
                        "No se pudo enviar el correo de recuperación.",
                        excepcion
                );
            }
            return;
        }

        // Si no hay API Key de Resend (Local), envía por Gmail SMTP
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(remitenteGmail);
        mensaje.setTo(destinatario);
        mensaje.setSubject("Recuperación de contraseña - Helpdesk Pro");
        mensaje.setText(cuerpoTexto);

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