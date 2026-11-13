package com.example.helpdeskpro.viemodel.ustate

import com.example.helpdeskpro.modelo.HomeTecnicoDTO

/**
 * Estado observable de la página principal del técnico.
 *
 * @param incidencias incidencias asignadas al técnico.
 * @param filtroEstado estado usado para filtrar, o null para mostrar todas.
 * @param isLoading indica si se están cargando las incidencias.
 * @param errorMessage error producido al cargar las incidencias.
 */
data class HomeTecnicoUiState(
    val incidencias: List<HomeTecnicoDTO> = emptyList(),
    val filtroEstado: String? = null,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val incidenciasFiltradas: List<HomeTecnicoDTO>
        get() {
            return incidencias
                .filter { incidencia ->
                    filtroEstado == null ||
                        normalizarEstado(incidencia.estado) == normalizarEstado(filtroEstado)
                }
        }

    private fun normalizarEstado(estado: String): String =
        estado.trim().uppercase().replace('_', ' ').replace(Regex("\\s+"), " ")
}
