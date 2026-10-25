package com.example.helpdeskpro.viemodel.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.helpdeskpro.modelo.IncidenciaDTO
import com.example.helpdeskpro.viemodel.ustate.HomeEmpleadoUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class HomeEmpleadoViewModel : ViewModel() {

    var uiState by mutableStateOf(HomeEmpleadoUiState())
        private set

    /**
     * Ejecuta la carga proporcionada por el futuro repositorio y actualiza el estado.
     * Evita lanzar varias cargas simultáneas desde la misma pantalla.
     */
    fun cargarIncidencias(cargar: suspend () -> List<IncidenciaDTO>) {
        if (uiState.isLoading) return

        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val incidencias = cargar()
                uiState = uiState.copy(
                    incidencias = incidencias,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = exception.message
                        ?: "No se pudieron cargar las incidencias"
                )
            }
        }
    }

    fun limpiarError() {
        uiState = uiState.copy(errorMessage = null)
    }
}
