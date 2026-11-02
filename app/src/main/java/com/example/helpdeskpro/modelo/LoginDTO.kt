package com.example.helpdeskpro.modelo

/**
 * Datos necesarios para solicitar el inicio de sesión.
 *
 * @param email correo electrónico de la cuenta.
 * @param password contraseña de la cuenta.
 */
data class LoginDTO(
    val email: String,
    val password: String
)
