package com.example.helpdeskpro.modelo

import kotlinx.serialization.Serializable

/** Datos de un usuario mostrados y editados desde la administración. */
@Serializable
data class AdminUsuarioDTO(
    val id: Long,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val telefono: String,
    val rol: String,
    val departamento: String,
    val activo: Boolean
)
