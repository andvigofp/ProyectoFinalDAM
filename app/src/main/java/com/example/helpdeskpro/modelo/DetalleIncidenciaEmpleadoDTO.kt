package com.example.helpdeskpro.modelo

/**
 * Datos de una incidencia tal y como se muestran al empleado que la creó.
 */
data class DetalleIncidenciaEmpleadoDTO(
    val id: Long,
    val titulo: String,
    val descripcion: String,
    val estado: String,
    val comentarios: List<ComentarioIncidenciaDTO> = emptyList(),
    val historial: List<HistorialIncidenciaDTO> = emptyList()
)
