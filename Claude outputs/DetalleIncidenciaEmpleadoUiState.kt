package com.example.helpdeskpro.viemodel.ustate

import com.example.helpdeskpro.modelo.DetalleIncidenciaEmpleadoDTO

/**
 * Estado único de la pantalla de detalle de incidencia del empleado.
 *
 * @param detalle datos de la incidencia mostrada.
 * @param comentario texto del comentario en edición.
 * @param comentarioError error de validación del comentario.
 * @param isLoading indica si se está cargando el detalle.
 * @param isSending indica si se está enviando un comentario.
 * @param errorMessage error general de carga o de la operación.
 */
data class DetalleIncidenciaEmpleadoUiState(
    val detalle: DetalleIncidenciaEmpleadoDTO? = null,
    val comentario: String = "",
    val comentarioError: String? = null,
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val errorMessage: String? = null
)
