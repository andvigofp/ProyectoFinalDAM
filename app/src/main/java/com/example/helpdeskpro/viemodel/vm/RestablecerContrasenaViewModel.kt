package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.helpdeskpro.R
import com.example.helpdeskpro.data.repository.authRepository.AuthRepository
import com.example.helpdeskpro.data.repository.authRepository.inmemory.AuthRepositoryInMemory
import com.example.helpdeskpro.data.repository.inmemoryRepository.InMemoryHelpdeskStore
import com.example.helpdeskpro.modelo.RestablecerContrasenaDTO
import com.example.helpdeskpro.viemodel.ustate.RestablecerContrasenaUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RestablecerContrasenaViewModel(
    application: Application,
    emailInicial: String,
    private val authRepository: AuthRepository = AuthRepositoryInMemory(InMemoryHelpdeskStore())
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(
        RestablecerContrasenaUiState(email = emailInicial)
    )
    val uiState: StateFlow<RestablecerContrasenaUiState> = _uiState.asStateFlow()

    fun onCodigoChange(codigo: String) {
        _uiState.update {
            it.copy(codigo = codigo.filter(Char::isDigit).take(6), codigoError = null, errorMessage = null)
        }
    }

    fun onNuevaContrasenaChange(contrasena: String) {
        _uiState.update {
            it.copy(
                nuevaContrasena = contrasena,
                contrasenaError = null,
                errorMessage = null
            )
        }
    }

    fun restablecerContrasena() {
        val state = _uiState.value
        val emailError = when {
            state.email.isBlank() ->
                getApplication<Application>().getString(R.string.error_email_required)
            !Patterns.EMAIL_ADDRESS.matcher(state.email.trim()).matches() ->
                getApplication<Application>().getString(R.string.error_email_invalid)
            else -> null
        }
        val codigoError = if (state.codigo.length != 6) {
            getApplication<Application>().getString(R.string.error_recovery_code)
        } else {
            null
        }
        val contrasenaError = when {
            state.nuevaContrasena.isBlank() ->
                getApplication<Application>().getString(R.string.error_new_password_required)
            state.nuevaContrasena.length < 6 ->
                getApplication<Application>().getString(R.string.error_password_length)
            else -> null
        }
        _uiState.update {
            it.copy(
                email = state.email.trim(),
                emailError = emailError,
                codigoError = codigoError,
                contrasenaError = contrasenaError,
                errorMessage = null,
                successMessage = null
            )
        }
        if (emailError != null || codigoError != null || contrasenaError != null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                authRepository.restablecerContrasena(
                    RestablecerContrasenaDTO(
                        email = state.email.trim(),
                        codigo = state.codigo,
                        nuevaContrasena = state.nuevaContrasena
                    )
                )
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        email = "",
                        codigo = "",
                        nuevaContrasena = "",
                        successMessage = getApplication<Application>()
                            .getString(R.string.password_reset_success)
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
                                .getString(R.string.error_password_reset)
                    )
                }
            }
        }
    }
}

class RestablecerContrasenaViewModelFactory(
    private val application: Application,
    private val email: String,
    private val authRepository: AuthRepository = AuthRepositoryInMemory(InMemoryHelpdeskStore())
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RestablecerContrasenaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RestablecerContrasenaViewModel(
                application,
                email,
                authRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
