package com.example.helpdeskpro.modelo

/**
 * Información del usuario que se muestra en su perfil.
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
