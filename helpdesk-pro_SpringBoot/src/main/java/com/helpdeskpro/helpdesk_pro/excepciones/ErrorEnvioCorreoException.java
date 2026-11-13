package com.helpdeskpro.helpdesk_pro.excepciones;

public class ErrorEnvioCorreoException extends RuntimeException {

    public ErrorEnvioCorreoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
