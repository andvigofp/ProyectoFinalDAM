package com.example.helpdeskpro.modelo

/**
 * Información resumida de una incidencia para los listados de la aplicación.
 *
 * @param id identificador único de la incidencia.
 * @param titulo título de la incidencia.
 * @param categoria categoría asignada.
 * @param prioridad prioridad asignada.
 * @param fecha fecha de creación de la incidencia.
 * @param estado estado actual de la incidencia.
 */
data class IncidenciaDTO(
    val id: Long,
    val titulo: String,
    val categoria: String,
    val prioridad: String,
    val fecha: String,
    val estado: String
)
