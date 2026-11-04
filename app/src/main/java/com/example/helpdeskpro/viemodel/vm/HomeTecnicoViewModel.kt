package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.HomeTecnicoDTO
import com.example.helpdeskpro.viemodel.ustate.HomeTecnicoUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Mantiene el estado y la carga de incidencias asignadas al técnico.
 *
 * @property uiState flujo observable del estado de la pantalla.
 * @param application aplicación Android usada para obtener recursos.
 * @param userId identificador del técnico autenticado.
 */
class HomeTecnicoViewModel(
    application: Application,
    val userId: String
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(HomeTecnicoUiState())
    val uiState: StateFlow<HomeTecnicoUiState> = _uiState.asStateFlow()

    /**
     * Carga las incidencias asignadas al técnico.
     *
     * @param cargar función suspendida que obtiene las incidencias.
     */
    fun cargarIncidencias(cargar: suspend () -> List<HomeTecnicoDTO>) {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

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
                                .getString(R.string.error_load_technician_incidents)
                    )
                }
            }
        }
    }

    /** Cambia el filtro de estado; null muestra todos los estados. */
    fun onFiltroEstadoChange(estado: String?) {
        _uiState.update { it.copy(filtroEstado = estado) }
    }

    /** Elimina el mensaje de error actual. */
    fun limpiarError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}

/**
 * Crea instancias de [HomeTecnicoViewModel] asociadas a un técnico.
 *
 * @param application aplicación Android asociada al ViewModel.
 * @param userId identificador del técnico.
 */
class HomeTecnicoViewModelFactory(
    private val application: Application,
    private val userId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeTecnicoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeTecnicoViewModel(application, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
