package com.example.helpdeskpro.modelo

/**
 * Datos necesarios para solicitar una contraseña temporal.
 *
 * @param email correo electrónico al que se asocia la cuenta.
 */
data class RecuperarContrasenaDTO(
    val email: String
)
