package com.example.helpdeskpro.modelo

/**
 * Información de una incidencia asignada a un técnico.
 *
 * @param id identificador único de la incidencia.
 * @param titulo título de la incidencia.
 * @param categoria categoría asignada.
 * @param prioridad prioridad asignada.
 * @param fecha fecha de creación de la incidencia.
 * @param estado estado actual de la incidencia.
 * @param creador nombre de la persona que creó la incidencia.
 */
data class HomeTecnicoDTO(
    val id: Long,
    val titulo: String,
    val categoria: String,
    val prioridad: String,
    val fecha: String,
    val estado: String,
    val creador: String
)
