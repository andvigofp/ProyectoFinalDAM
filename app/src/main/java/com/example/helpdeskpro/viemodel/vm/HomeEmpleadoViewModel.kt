package com.example.helpdeskpro.viemodel.vm


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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

/**
 * Mantiene el estado y la carga de incidencias del empleado.
 *
 * @property uiState flujo observable del estado de la pantalla.
 * @param application aplicación Android usada para obtener recursos.
 * @param userId identificador del empleado.
 */
class HomeEmpleadoViewModel(
    application: Application,
    val userId: String
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(HomeEmpleadoUiState())
    val uiState: StateFlow<HomeEmpleadoUiState> = _uiState.asStateFlow()

    /**
     * Carga las incidencias del empleado.
     * Evita lanzar varias cargas simultáneas.
     *
     * @param cargar función suspendida que obtiene la lista de incidencias.
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

    /** Elimina el mensaje de error actual. */
    fun limpiarError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}

/**
 * Crea instancias de [HomeEmpleadoViewModel] asociadas a un usuario.
 *
 * @param application aplicación Android asociada al ViewModel.
 * @param userId identificador del empleado.
 */
class HomeEmpleadoViewModelFactory(
    private val application: Application,
    private val userId: String
) : ViewModelProvider.Factory {

    /**
     * Crea el ViewModel solicitado o falla si el tipo no está soportado.
     *
     * @param modelClass clase del ViewModel que se debe crear.
     * @return instancia del tipo solicitado.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeEmpleadoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeEmpleadoViewModel(
                application = application,
                userId = userId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}