package com.example.helpdeskpro.modelo

/**
 * Información resumida de una incidencia para los listados de la aplicación.
 */
data class IncidenciaDTO(
    val id: Long,
    val titulo: String,
    val categoria: String,
    val prioridad: String,
    val fecha: String,
    val estado: String
)
