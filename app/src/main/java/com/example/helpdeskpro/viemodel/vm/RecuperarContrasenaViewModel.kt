package com.example.helpdeskpro.viemodel.vm

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.helpdeskpro.modelo.RecuperarContrasenaDTO
import com.example.helpdeskpro.viemodel.ustate.RecuperarContrasenaUiState

class RecuperarContrasenaViewModel : ViewModel() {

    var uiState by mutableStateOf(RecuperarContrasenaUiState())
        private set

    fun onEmailChange(email: String) {
        uiState = uiState.copy(
            email = email,
            emailError = null,
            errorMessage = null,
            successMessage = null
        )
    }

    /**
     * Valida el email y entrega el DTO cuando es correcto.
     * La llamada al repositorio/API se añadirá en la iteración de integración.
     */
    fun recuperarContrasena(onSolicitudValida: (RecuperarContrasenaDTO) -> Unit = {}) {
        val normalizedEmail = uiState.email.trim()

        val emailError = when {
            normalizedEmail.isEmpty() -> "Introduce tu email"
            !Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches() ->
                "Introduce un email válido"
            else -> null
        }

        uiState = uiState.copy(
            email = normalizedEmail,
            emailError = emailError,
            errorMessage = null,
            successMessage = null
        )

        if (emailError == null) {
            onSolicitudValida(
                RecuperarContrasenaDTO(email = normalizedEmail)
            )
        }
    }
}
