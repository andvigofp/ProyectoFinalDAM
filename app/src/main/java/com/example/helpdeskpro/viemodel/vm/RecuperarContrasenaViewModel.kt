package com.example.helpdeskpro.viemodel.vm


import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.RecuperarContrasenaDTO
import com.example.helpdeskpro.viemodel.ustate.RecuperarContrasenaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class RecuperarContrasenaViewModel(application: Application) :
    AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(RecuperarContrasenaUiState())
    val uiState: StateFlow<RecuperarContrasenaUiState> = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = null,
                errorMessage = null,
                successMessage = null
            )
        }
    }

    /**
     * Valida el email y entrega el DTO cuando es correcto.
     * La llamada al repositorio/API se añadirá en la iteración de integración.
     */
    fun recuperarContrasena(
        onSolicitudValida: (RecuperarContrasenaDTO) -> Unit = {}
    ) {
        val normalizedEmail = _uiState.value.email.trim()

        val emailError = when {
            normalizedEmail.isEmpty() ->
                getApplication<Application>()
                    .getString(R.string.error_email_required)

            !Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches() ->
                getApplication<Application>()
                    .getString(R.string.error_email_invalid)

            else -> null
        }

        _uiState.update {
            it.copy(
                email = normalizedEmail,
                emailError = emailError,
                errorMessage = null,
                successMessage = null
            )
        }

        if (emailError == null) {
            onSolicitudValida(
                RecuperarContrasenaDTO(email = normalizedEmail)
            )
        }
    }
}