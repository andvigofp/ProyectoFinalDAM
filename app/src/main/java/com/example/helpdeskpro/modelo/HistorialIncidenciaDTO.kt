package com.example.helpdeskpro.modelo

/**
 * Entrada del historial de cambios de una incidencia.
 *
 * @param fecha fecha en que ocurrió el cambio.
 * @param descripcion descripción del cambio realizado.
 */
data class HistorialIncidenciaDTO(
    val fecha: String,
    val descripcion: String
)
