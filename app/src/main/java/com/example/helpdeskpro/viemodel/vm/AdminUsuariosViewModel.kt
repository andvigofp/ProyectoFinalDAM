package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.helpdeskpro.data.repository.adminRepository.AdministracionRepository
import com.example.helpdeskpro.data.repository.adminRepository.inmemory.AdministracionRepositoryInMemory
import com.example.helpdeskpro.data.repository.inmemoryRepository.InMemoryHelpdeskStore
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.AdminUsuarioDTO
import com.example.helpdeskpro.viemodel.ustate.AdminUsuariosUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AdminUsuariosViewModel(
    application: Application,
    private val administracionRepository: AdministracionRepository =
        AdministracionRepositoryInMemory(InMemoryHelpdeskStore())
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(AdminUsuariosUiState())
    val uiState: StateFlow<AdminUsuariosUiState> = _uiState.asStateFlow()
    private var loadJob: Job? = null

    fun onSearchQueryChange(query: String) {
        if (query == _uiState.value.searchQuery) return
        _uiState.update {
            it.copy(
                searchQuery = query,
                usuarios = emptyList(),
                isLoading = true,
                errorMessage = null
            )
        }
        cargarUsuarios(query, debounce = true)
    }

    fun cargarUsuarios() {
        cargarUsuarios(_uiState.value.searchQuery, debounce = false)
    }

    private fun cargarUsuarios(busqueda: String, debounce: Boolean) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            if (debounce) delay(300)
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                _uiState.update {
                    it.copy(
                        usuarios = administracionRepository.obtenerUsuarios(busqueda),
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
                            ?: getApplication<Application>().getString(R.string.error_load_admin_users)
                    )
                }
            }
        }
    }

    fun guardarUsuario(
        usuario: AdminUsuarioDTO,
        password: String? = null,
        onGuardado: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val usuarioGuardado = administracionRepository.guardarUsuario(usuario, password)
                _uiState.update { state ->
                    val existe = state.usuarios.any { it.id == usuarioGuardado.id }
                    val usuarios = if (existe) {
                        state.usuarios.map {
                            if (it.id == usuarioGuardado.id) usuarioGuardado else it
                        }
                    } else {
                        state.usuarios + usuarioGuardado
                    }
                    state.copy(usuarios = usuarios, errorMessage = null)
                }
                onGuardado()
                cargarUsuarios()
            } catch (exception: kotlinx.coroutines.CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = exception.message
                            ?: getApplication<Application>().getString(R.string.error_save_admin_user)
                    )
                }
            }
        }
    }

    fun siguienteId(): Long = administracionRepository.siguienteUsuarioId()
}

class AdminUsuariosViewModelFactory(
    private val application: Application,
    private val administracionRepository: AdministracionRepository =
        AdministracionRepositoryInMemory(InMemoryHelpdeskStore())
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminUsuariosViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AdminUsuariosViewModel(application, administracionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
