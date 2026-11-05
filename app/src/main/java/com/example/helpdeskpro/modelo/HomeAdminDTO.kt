package com.example.helpdeskpro.modelo

import kotlinx.serialization.Serializable

/** Recuento de incidencias por estado mostrado en el panel de administración. */
@Serializable
data class HomeAdminDTO(
    val abiertas: Int,
    val enProgreso: Int,
    val resueltas: Int
)
