package com.example.helpdeskpro.modelo

/**
 * Detalle completo de una incidencia para la vista del empleado.
 */
data class DetalleIncidenciaDTO(
    val id: Long,
    val titulo: String,
    val descripcion: String,
    val estado: String,
    val comentarios: List<ComentarioIncidenciaDTO> = emptyList(),
    val historial: List<HistorialIncidenciaDTO> = emptyList()
)
