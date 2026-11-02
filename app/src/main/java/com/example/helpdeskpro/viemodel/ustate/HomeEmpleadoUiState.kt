package com.example.helpdeskpro.viemodel.ustate

import com.example.helpdeskpro.modelo.IncidenciaDTO

/**
 * Estado único de la página principal del empleado.
 *
 * @param incidencias incidencias disponibles para mostrar.
 * @param isLoading indica si se están cargando incidencias.
 * @param errorMessage error producido al cargar las incidencias.
 */
data class HomeEmpleadoUiState(
    val incidencias: List<IncidenciaDTO> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
