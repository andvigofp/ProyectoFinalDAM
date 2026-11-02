package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.PerfilDTO
import com.example.helpdeskpro.viemodel.ustate.PerfilUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Mantiene los datos del perfil del usuario autenticado.
 *
 * @property uiState flujo observable del estado del perfil.
 * @param application aplicación Android usada para obtener recursos.
 * @param userId identificador del usuario cuyo perfil se muestra.
 */
class PerfilViewModel(
    application: Application,
    val userId: String
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    /**
     * Ejecuta la carga proporcionada por el futuro repositorio del perfil.
     *
     * @param cargar función suspendida que obtiene los datos del perfil.
     */
    fun cargarPerfil(cargar: suspend () -> PerfilDTO) {

        if (_uiState.value.isLoading) return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val perfil = cargar()

                _uiState.update {
                    it.copy(
                        perfil = perfil,
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
                                .getString(R.string.error_load_profile)
                    )
                }
            }
        }
    }

    /**
     * Actualiza los datos mostrados después de una edición correcta.
     *
     * @param perfil datos actualizados del perfil.
     */
    fun actualizarPerfil(perfil: PerfilDTO) {
        _uiState.update {
            it.copy(
                perfil = perfil,
                errorMessage = null
            )
        }
    }

    /** Elimina el mensaje de error actual. */
    fun limpiarError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }

    /** Elimina los datos locales del perfil al cerrar la sesión. */
    fun limpiarPerfil() {
        _uiState.value = PerfilUiState()
    }
}

/**
 * Crea instancias de [PerfilViewModel] asociadas a un usuario.
 *
 * @param application aplicación Android asociada al ViewModel.
 * @param userId identificador del usuario cuyo perfil se gestiona.
 */
class PerfilViewModelFactory(
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
        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerfilViewModel(
                application = application,
                userId = userId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}