package com.example.helpdeskpro.viemodel.vm


import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.RecuperarContrasenaDTO
import com.example.helpdeskpro.viemodel.ustate.RecuperarContrasenaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Mantiene el formulario y valida las solicitudes de recuperación de contraseña.
 *
 * @property uiState flujo observable del estado del formulario.
 * @param application aplicación Android usada para acceder a recursos.
 */
class RecuperarContrasenaViewModel(application: Application) :
    AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(RecuperarContrasenaUiState())
    val uiState: StateFlow<RecuperarContrasenaUiState> = _uiState.asStateFlow()

    /**
     * Actualiza el email y limpia los mensajes anteriores.
     *
     * @param email correo electrónico introducido.
     */
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
     *
     * @param onSolicitudValida acción que recibe la solicitud válida.
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

/**
 * Crea instancias de [RecuperarContrasenaViewModel].
 *
 * @param application aplicación Android asociada al ViewModel.
 */
class RecuperarContrasenaViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    /**
     * Crea el ViewModel solicitado o falla si el tipo no está soportado.
     *
     * @param modelClass clase del ViewModel que se debe crear.
     * @return instancia del tipo solicitado.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecuperarContrasenaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecuperarContrasenaViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}