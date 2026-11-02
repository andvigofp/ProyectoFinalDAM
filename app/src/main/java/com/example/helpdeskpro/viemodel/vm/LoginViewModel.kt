package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.LoginDTO
import com.example.helpdeskpro.viemodel.ustate.LoginUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Mantiene el estado y valida las credenciales del formulario de inicio de sesión.
 *
 * @property uiState flujo observable del estado del formulario.
 * @param application aplicación Android usada para acceder a recursos.
 */
class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Actualiza el email y limpia su error de validación.
     *
     * @param email correo electrónico introducido.
     */
    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = null,
                errorMessage = null,
                loginSuccess = false
            )
        }
    }

    /**
     * Actualiza la contraseña y limpia su error de validación.
     *
     * @param password contraseña introducida.
     */
    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = null,
                errorMessage = null,
                loginSuccess = false
            )
        }
    }

    /**
     * Valida el formulario y entrega las credenciales cuando son correctas.
     * La llamada al repositorio/API se añadirá en la iteración de integración.
     *
     * @param onCredencialesValidas acción que recibe las credenciales válidas.
     */
    fun iniciarSesion(onCredencialesValidas: (LoginDTO) -> Unit = {}) {

        val currentState = _uiState.value
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

        _uiState.update {
            it.copy(
                email = normalizedEmail,
                emailError = emailError,
                passwordError = passwordError,
                errorMessage = null,
                loginSuccess = false
            )
        }

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

/**
 * Crea instancias de [LoginViewModel].
 *
 * @param application aplicación Android asociada al ViewModel.
 */
class LoginViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    /**
     * Crea el ViewModel solicitado o falla si el tipo no está soportado.
     *
     * @param modelClass clase del ViewModel que se debe crear.
     * @return instancia del tipo solicitado.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}