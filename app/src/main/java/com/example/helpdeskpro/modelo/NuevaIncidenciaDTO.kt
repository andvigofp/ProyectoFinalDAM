package com.example.helpdeskpro.modelo

/**
 * Datos necesarios para que un empleado registre una nueva incidencia.
 *
 * @param titulo título de la incidencia.
 * @param descripcion descripción del problema.
 * @param categoria categoría seleccionada.
 * @param prioridad prioridad seleccionada.
 */
data class NuevaIncidenciaDTO(
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val prioridad: String
)
