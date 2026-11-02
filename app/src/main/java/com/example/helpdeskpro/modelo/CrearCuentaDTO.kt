package com.example.helpdeskpro.modelo

/**
 * Datos necesarios para registrar una nueva cuenta de usuario.
 *
 * @param nombre nombre del usuario.
 * @param apellidos apellidos del usuario.
 * @param email correo electrónico de la cuenta.
 * @param telefono teléfono de contacto.
 * @param password contraseña elegida para la cuenta.
 * @param rol rol asignado al usuario.
 * @param departamento departamento al que pertenece.
 */
data class CrearCuentaDTO(
    val nombre: String,
    val apellidos: String,
    val email: String,
    val telefono: String,
    val password: String,
    val rol: String,
    val departamento: String
)
