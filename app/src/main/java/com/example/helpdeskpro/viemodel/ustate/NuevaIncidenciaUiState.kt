package com.example.helpdeskpro.viemodel.ustate

/**
 * Estado único del formulario de una nueva incidencia.
 *
 * @param titulo título introducido.
 * @param descripcion descripción introducida.
 * @param categoria categoría seleccionada.
 * @param prioridad prioridad seleccionada.
 * @param tituloError error de validación del título.
 * @param descripcionError error de validación de la descripción.
 * @param categoriaError error de validación de la categoría.
 * @param prioridadError error de validación de la prioridad.
 * @param isLoading indica si hay una operación en curso.
 * @param errorMessage error general de la operación.
 * @param incidenciaCreada indica si la incidencia se ha creado.
 */
data class NuevaIncidenciaUiState(
    val titulo: String="",
    val descripcion: String="",
    val categoria: String="",
    val prioridad: String="",
    val tituloError: String? = null,
    val descripcionError: String? = null,
    val categoriaError: String? = null,
    val prioridadError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val incidenciaCreada: Boolean = false
)
