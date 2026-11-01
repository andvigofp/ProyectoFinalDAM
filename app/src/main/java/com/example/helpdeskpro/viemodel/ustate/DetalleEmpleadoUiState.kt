package com.example.helpdeskpro.viemodel.ustate

import com.example.helpdeskpro.modelo.DetalleIncidenciaDTO

/**
 * Estado único de la pantalla de detalle de incidencia del empleado.
 */
data class DetalleEmpleadoUiState(
    val detalle: DetalleIncidenciaDTO? = null,
    val comentario: String = "",
    val comentarioError: String? = null,
    val isLoading: Boolean = false,
    val isSending: Boolean = false,
    val errorMessage: String? = null
)
