package com.example.helpdeskpro.viemodel.ustate

import com.example.helpdeskpro.modelo.IncidenciaDTO

/**
 * Estado único de la página principal del empleado.
 */
data class HomeEmpleadoUiState(
    val incidencias: List<IncidenciaDTO> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
