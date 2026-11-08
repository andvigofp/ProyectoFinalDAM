package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.helpdeskpro.data.repository.inmemoryRepository.InMemoryHelpdeskStore
import com.example.helpdeskpro.data.repository.perfilRepository.PerfilRepository
import com.example.helpdeskpro.data.repository.perfilRepository.inmemory.PerfilRepositoryInMemory
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.EditarPerfilDTO
import com.example.helpdeskpro.modelo.PerfilDTO
import com.example.helpdeskpro.viemodel.ustate.EditarPerfilUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

class EditarPerfilViewModel(
    application: Application,
    val userId: String,
    private val perfilRepository: PerfilRepository =
        PerfilRepositoryInMemory(InMemoryHelpdeskStore())
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(EditarPerfilUiState())
    val uiState: StateFlow<EditarPerfilUiState> = _uiState.asStateFlow()

    fun inicializar(perfil: PerfilDTO) {
        if (_uiState.value.perfil?.id != perfil.id) {
            _uiState.value = EditarPerfilUiState(perfil = perfil)
        }
    }

    fun onNombreChange(value: String) = actualizarPerfil { it.copy(nombre = value) }
    fun onApellidosChange(value: String) = actualizarPerfil { it.copy(apellidos = value) }
    fun onEmailChange(value: String) = actualizarPerfil { it.copy(email = value) }
    fun onTelefonoChange(value: String) = actualizarPerfil { it.copy(telefono = value) }

    fun onContrasenaChange(value: String) {
        _uiState.update { it.copy(contrasena = value, contrasenaError = null, errorMessage = null) }
    }

    fun guardarPerfil(onDatosValidos: (EditarPerfilDTO) -> Unit) {
        val state = _uiState.value
        val perfil = state.perfil ?: return
        val nombre = perfil.nombre.trim()
        val apellidos = perfil.apellidos.trim()
        val email = perfil.email.trim()
        val telefono = perfil.telefono.trim()
        val contrasena = state.contrasena

        val nombreError = if (nombre.isEmpty()) {
            getApplication<Application>().getString(R.string.error_name_required)
        } else null
        val apellidosError = if (apellidos.isEmpty()) {
            getApplication<Application>().getString(R.string.error_surnames_required)
        } else null
        val emailError = when {
            email.isEmpty() -> getApplication<Application>().getString(R.string.error_email_required)
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                getApplication<Application>().getString(R.string.error_email_invalid)
            else -> null
        }
        val telefonoError = if (telefono.isEmpty()) {
            getApplication<Application>().getString(R.string.error_phone_required)
        } else null
        val contrasenaError = if (contrasena.isNotEmpty() && contrasena.length < 6) {
            getApplication<Application>().getString(R.string.error_password_length)
        } else null

        _uiState.update {
            it.copy(
                perfil = perfil.copy(
                    nombre = nombre,
                    apellidos = apellidos,
                    email = email,
                    telefono = telefono
                ),
                nombreError = nombreError,
                apellidosError = apellidosError,
                emailError = emailError,
                telefonoError = telefonoError,
                contrasenaError = contrasenaError,
                errorMessage = null
            )
        }

        if (listOf(nombreError, apellidosError, emailError, telefonoError, contrasenaError)
                .all { it == null }
        ) {
            val datos = EditarPerfilDTO(
                    id = perfil.id,
                    nombre = nombre,
                    apellidos = apellidos,
                    email = email,
                    telefono = telefono,
                    contrasena = contrasena.takeIf { it.isNotBlank() }
                )
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                try {
                    val perfilActualizado = perfilRepository.actualizarPerfil(datos)
                    _uiState.update {
                        it.copy(perfil = perfilActualizado, isLoading = false, errorMessage = null)
                    }
                    onDatosValidos(datos)
                } catch (exception: CancellationException) {
                    throw exception
                } catch (exception: Exception) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message
                                ?: getApplication<Application>().getString(R.string.error_save_profile)
                        )
                    }
                }
            }
        }
    }

    private fun actualizarPerfil(transform: (PerfilDTO) -> PerfilDTO) {
        _uiState.update { state ->
            state.perfil?.let { perfil ->
                state.copy(
                    perfil = transform(perfil),
                    nombreError = null,
                    apellidosError = null,
                    emailError = null,
                    telefonoError = null,
                    errorMessage = null
                )
            } ?: state
        }
    }
}

class EditarPerfilViewModelFactory(
    private val application: Application,
    private val userId: String,
    private val perfilRepository: PerfilRepository =
        PerfilRepositoryInMemory(InMemoryHelpdeskStore())
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditarPerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EditarPerfilViewModel(application, userId, perfilRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
