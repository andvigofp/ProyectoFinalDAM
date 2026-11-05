package com.example.helpdeskpro.modelo

import kotlinx.serialization.Serializable

/** Cambios enviados al actualizar los datos personales del usuario autenticado. */
@Serializable
data class EditarPerfilDTO(
    val id: Long,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val telefono: String,
    val contrasena: String? = null
)
