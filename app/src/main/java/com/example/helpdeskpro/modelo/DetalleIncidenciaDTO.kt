package com.example.helpdeskpro.modelo

/**
 * Detalle completo de una incidencia para la vista del empleado.
 *
 * @param id identificador único de la incidencia.
 * @param titulo título de la incidencia.
 * @param descripcion descripción del problema.
 * @param estado estado actual de la incidencia.
 * @param creador nombre de quien creó la incidencia.
 * @param departamento departamento del creador.
 * @param categoria categoría asignada a la incidencia.
 * @param prioridad prioridad asignada.
 * @param fechaCreacion fecha en que se creó la incidencia.
 * @param comentarios comentarios asociados a la incidencia.
 * @param historial cambios registrados para la incidencia.
 */
data class DetalleIncidenciaDTO(
    val id: Long,
    val titulo: String,
    val descripcion: String,
    val estado: String,
    val creador: String,
    val departamento: String,
    val categoria: String,
    val prioridad: String,
    val fechaCreacion: String,
    val comentarios: List<ComentarioIncidenciaDTO> = emptyList(),
    val historial: List<HistorialIncidenciaDTO> = emptyList()
)