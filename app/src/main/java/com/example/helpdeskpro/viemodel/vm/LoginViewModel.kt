package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.LoginDTO
import com.example.helpdeskpro.viemodel.ustate.LoginUiState

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(email: String) {
        uiState = uiState.copy(
            email = email,
            emailError = null,
            errorMessage = null,
            loginSuccess = false
        )
    }

    fun onPasswordChange(password: String) {
        uiState = uiState.copy(
            password = password,
            passwordError = null,
            errorMessage = null,
            loginSuccess = false
        )
    }

    /**
     * Valida el formulario y entrega las credenciales cuando son correctas.
     * La llamada al repositorio/API se añadirá en la iteración de integración.
     */
    fun iniciarSesion(onCredencialesValidas: (LoginDTO) -> Unit = {}) {
        val currentState = uiState
        val normalizedEmail = currentState.email.trim()

        val emailError = when {
            normalizedEmail.isEmpty() ->
                getApplication<Application>().getString(R.string.error_email_required)
            !Patterns.EMAIL_ADDRESS.matcher(normalizedEmail).matches() ->
                getApplication<Application>().getString(R.string.error_email_invalid)
            else -> null
        }
        val passwordError = if (currentState.password.isBlank()) {
            getApplication<Application>().getString(R.string.error_password_required)
        } else {
            null
        }

        uiState = uiState.copy(
            email = normalizedEmail,
            emailError = emailError,
            passwordError = passwordError,
            errorMessage = null,
            loginSuccess = false
        )

        if (emailError == null && passwordError == null) {
            onCredencialesValidas(
                LoginDTO(
                    email = normalizedEmail,
                    password = currentState.password
                )
            )
        }
    }
}
