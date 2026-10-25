package com.example.helpdeskpro.modelo

/**
 * Datos necesarios para que un empleado registre una nueva incidencia.
 */
data class NuevaIncidenciaDTO(
    val titulo: String,
    val descripcion: String,
    val categoria: String,
    val prioridad: String
)
