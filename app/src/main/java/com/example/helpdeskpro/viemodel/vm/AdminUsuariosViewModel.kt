package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.AdminUsuarioDTO
import com.example.helpdeskpro.viemodel.ustate.AdminUsuariosUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminUsuariosViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(AdminUsuariosUiState())
    val uiState: StateFlow<AdminUsuariosUiState> = _uiState.asStateFlow()

    fun cargarUsuarios(cargar: suspend () -> List<AdminUsuarioDTO>) {
        if (_uiState.value.isLoading || _uiState.value.usuarios.isNotEmpty()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                _uiState.update {
                    it.copy(usuarios = cargar(), isLoading = false, errorMessage = null)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message
                            ?: getApplication<Application>().getString(R.string.error_load_admin_users)
                    )
                }
            }
        }
    }

    fun guardarUsuario(usuario: AdminUsuarioDTO) {
        _uiState.update { state ->
            val existe = state.usuarios.any { it.id == usuario.id }
            val usuarios = if (existe) {
                state.usuarios.map { if (it.id == usuario.id) usuario else it }
            } else {
                state.usuarios + usuario
            }
            state.copy(usuarios = usuarios, errorMessage = null)
        }
    }

    fun siguienteId(): Long = (_uiState.value.usuarios.maxOfOrNull { it.id } ?: 0L) + 1L
}

class AdminUsuariosViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminUsuariosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminUsuariosViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
