package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
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

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    /**
     * Ejecuta la carga proporcionada por el futuro repositorio del perfil.
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

    /** Actualiza los datos mostrados después de una edición correcta. */
    fun actualizarPerfil(perfil: PerfilDTO) {
        _uiState.update {
            it.copy(
                perfil = perfil,
                errorMessage = null
            )
        }
    }

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