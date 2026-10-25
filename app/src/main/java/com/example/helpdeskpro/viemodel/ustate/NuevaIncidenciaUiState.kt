package com.example.helpdeskpro.viemodel.ustate

/**
 * Estado único del formulario de una nueva incidencia.
 */
data class NuevaIncidenciaUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    val prioridad: String = "",
    val tituloError: String? = null,
    val descripcionError: String? = null,
    val categoriaError: String? = null,
    val prioridadError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val incidenciaCreada: Boolean = false
)
