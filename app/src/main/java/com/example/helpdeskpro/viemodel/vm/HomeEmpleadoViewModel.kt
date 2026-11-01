package com.example.helpdeskpro.viemodel.vm


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.IncidenciaDTO
import com.example.helpdeskpro.viemodel.ustate.HomeEmpleadoUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeEmpleadoViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(HomeEmpleadoUiState())
    val uiState: StateFlow<HomeEmpleadoUiState> = _uiState.asStateFlow()

    /**
     * Carga las incidencias del empleado.
     * Evita lanzar varias cargas simultáneas.
     */
    fun cargarIncidencias(cargar: suspend () -> List<IncidenciaDTO>) {

        if (_uiState.value.isLoading) return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val incidencias = cargar()

                _uiState.update {
                    it.copy(
                        incidencias = incidencias,
                        isLoading = false,
                        errorMessage = null
                    )
                }

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message
                            ?: getApplication<Application>()
                                .getString(R.string.error_load_incidents)
                    )
                }
            }
        }
    }

    fun limpiarError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}
