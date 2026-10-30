package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.NuevaIncidenciaDTO
import com.example.helpdeskpro.viemodel.ustate.NuevaIncidenciaUiState

class NuevaIncidenciaViewModel(application: Application) : AndroidViewModel(application) {

    var uiState by mutableStateOf(NuevaIncidenciaUiState())
        private set

    fun onTituloChange(titulo: String) {
        uiState = uiState.copy(
            titulo = titulo,
            tituloError = null,
            errorMessage = null,
            incidenciaCreada = false
        )
    }

    fun onDescripcionChange(descripcion: String) {
        uiState = uiState.copy(
            descripcion = descripcion,
            descripcionError = null,
            errorMessage = null,
            incidenciaCreada = false
        )
    }

    fun onCategoriaChange(categoria: String) {
        uiState = uiState.copy(
            categoria = categoria,
            categoriaError = null,
            errorMessage = null,
            incidenciaCreada = false
        )
    }

    fun onPrioridadChange(prioridad: String) {
        uiState = uiState.copy(
            prioridad = prioridad,
            prioridadError = null,
            errorMessage = null,
            incidenciaCreada = false
        )
    }

    /**
     * Valida el formulario y entrega el DTO cuando los datos son correctos.
     * La llamada al repositorio/API se añadirá en la iteración de integración.
     */
    fun crearIncidencia(onDatosValidos: (NuevaIncidenciaDTO) -> Unit = {}) {
        val currentState = uiState
        val titulo = currentState.titulo.trim()
        val descripcion = currentState.descripcion.trim()
        val categoria = currentState.categoria.trim()
        val prioridad = currentState.prioridad.trim()

        val tituloError = if (titulo.isEmpty()) {
            getApplication<Application>().getString(R.string.error_title_required)
        } else {
            null
        }
        val descripcionError = if (descripcion.isEmpty()) {
            getApplication<Application>().getString(R.string.error_description_required)
        } else {
            null
        }
        val categoriaError = if (categoria.isEmpty()) {
            getApplication<Application>().getString(R.string.error_category_required)
        } else {
            null
        }
        val prioridadError = if (prioridad.isEmpty()) {
            getApplication<Application>().getString(R.string.error_priority_required)
        } else {
            null
        }

        uiState = currentState.copy(
            titulo = titulo,
            descripcion = descripcion,
            categoria = categoria,
            prioridad = prioridad,
            tituloError = tituloError,
            descripcionError = descripcionError,
            categoriaError = categoriaError,
            prioridadError = prioridadError,
            errorMessage = null,
            incidenciaCreada = false
        )

        val formularioValido = listOf(
            tituloError,
            descripcionError,
            categoriaError,
            prioridadError
        ).all { it == null }

        if (formularioValido) {
            onDatosValidos(
                NuevaIncidenciaDTO(
                    titulo = titulo,
                    descripcion = descripcion,
                    categoria = categoria,
                    prioridad = prioridad
                )
            )
        }
    }

    fun limpiarFormulario() {
        uiState = NuevaIncidenciaUiState()
    }
}
