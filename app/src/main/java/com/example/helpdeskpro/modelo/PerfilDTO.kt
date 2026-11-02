package com.example.helpdeskpro.modelo

/**
 * Información del usuario que se muestra en su perfil.
 *
 * @param id identificador único del usuario.
 * @param nombre nombre del usuario.
 * @param apellidos apellidos del usuario.
 * @param email correo electrónico de la cuenta.
 * @param telefono teléfono de contacto.
 * @param departamento departamento del usuario.
 * @param miembroDesde fecha de alta del usuario.
 * @param rol rol asignado al usuario.
 */
data class PerfilDTO(
    val id: Long,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val telefono: String,
    val departamento: String,
    val miembroDesde: String,
    val rol: String
)
