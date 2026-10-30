package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.PerfilDTO
import com.example.helpdeskpro.viemodel.ustate.PerfilUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class PerfilViewModel(application: Application) : AndroidViewModel(application) {

    var uiState by mutableStateOf(PerfilUiState())
        private set

    /**
     * Ejecuta la carga proporcionada por el futuro repositorio del perfil.
     */
    fun cargarPerfil(cargar: suspend () -> PerfilDTO) {
        if (uiState.isLoading) return

        viewModelScope.launch {
            uiState = uiState.copy(
                isLoading = true,
                errorMessage = null
            )

            try {
                val perfil = cargar()
                uiState = uiState.copy(
                    perfil = perfil,
                    isLoading = false,
                    errorMessage = null
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = exception.message
                        ?: getApplication<Application>().getString(R.string.error_load_profile)
                )
            }
        }
    }

    /** Actualiza los datos mostrados después de una edición correcta. */
    fun actualizarPerfil(perfil: PerfilDTO) {
        uiState = uiState.copy(
            perfil = perfil,
            errorMessage = null
        )
    }

    fun limpiarError() {
        uiState = uiState.copy(errorMessage = null)
    }

    /** Elimina los datos locales del perfil al cerrar la sesión. */
    fun limpiarPerfil() {
        uiState = PerfilUiState()
    }
}
