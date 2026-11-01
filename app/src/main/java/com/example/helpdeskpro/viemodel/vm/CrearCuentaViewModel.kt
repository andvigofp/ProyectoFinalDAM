package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.CrearCuentaDTO
import com.example.helpdeskpro.viemodel.ustate.CrearCuentaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CrearCuentaViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(CrearCuentaUiState())
    val uiState: StateFlow<CrearCuentaUiState> = _uiState.asStateFlow()

    fun onNombreChange(nombre: String) {
        _uiState.update {
            it.copy(
                nombre = nombre,
                nombreError = null,
                errorMessage = null,
                cuentaCreada = false
            )
        }
    }

    fun onApellidosChange(apellidos: String) {
        _uiState.update {
            it.copy(
                apellidos = apellidos,
                apellidosError = null,
                errorMessage = null,
                cuentaCreada = false
            )
        }
    }

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError = null,
                errorMessage = null,
                cuentaCreada = false
            )
        }
    }

    fun onTelefonoChange(telefono: String) {
        _uiState.update {
            it.copy(
                telefono = telefono,
                telefonoError = null,
                errorMessage = null,
                cuentaCreada = false
            )
        }
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = null,
                errorMessage = null,
                cuentaCreada = false
            )
        }
    }

    fun onRolChange(rol: String) {
        _uiState.update {
            it.copy(
                rol = rol,
                rolError = null,
                errorMessage = null,
                cuentaCreada = false
            )
        }
    }

    fun onDepartamentoChange(departamento: String) {
        _uiState.update {
            it.copy(
                departamento = departamento,
                departamentoError = null,
                errorMessage = null,
                cuentaCreada = false
            )
        }
    }

    /**
     * Valida el formulario y entrega el DTO cuando todos los datos son correctos.
     * La llamada al repositorio/API se añadirá en la iteración de integración.
     */
    fun crearCuenta(onDatosValidos: (CrearCuentaDTO) -> Unit = {}) {

        val currentState = _uiState.value

        val nombre = currentState.nombre.trim()
        val apellidos = currentState.apellidos.trim()
        val email = currentState.email.trim()
        val telefono = currentState.telefono.trim()
        val rol = currentState.rol.trim()
        val departamento = currentState.departamento.trim()

        val nombreError = if (nombre.isEmpty()) {
            getApplication<Application>().getString(R.string.error_name_required)
        } else {
            null
        }

        val apellidosError = if (apellidos.isEmpty()) {
            getApplication<Application>().getString(R.string.error_surnames_required)
        } else {
            null
        }

        val emailError = when {
            email.isEmpty() ->
                getApplication<Application>().getString(R.string.error_email_required)

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                getApplication<Application>().getString(R.string.error_email_invalid)

            else -> null
        }

        val telefonoError = if (telefono.isEmpty()) {
            getApplication<Application>().getString(R.string.error_phone_required)
        } else {
            null
        }

        val passwordError = when {
            currentState.password.isBlank() ->
                getApplication<Application>().getString(R.string.error_new_password_required)

            currentState.password.length < 6 ->
                getApplication<Application>().getString(R.string.error_password_length)

            else -> null
        }

        val rolError = if (rol.isEmpty()) {
            getApplication<Application>().getString(R.string.error_role_required)
        } else {
            null
        }

        val departamentoError = if (departamento.isEmpty()) {
            getApplication<Application>().getString(R.string.error_department_required)
        } else {
            null
        }

        _uiState.update {
            it.copy(
                nombre = nombre,
                apellidos = apellidos,
                email = email,
                telefono = telefono,
                rol = rol,
                departamento = departamento,
                nombreError = nombreError,
                apellidosError = apellidosError,
                emailError = emailError,
                telefonoError = telefonoError,
                passwordError = passwordError,
                rolError = rolError,
                departamentoError = departamentoError,
                errorMessage = null,
                cuentaCreada = false
            )
        }

        val formularioValido = listOf(
            nombreError,
            apellidosError,
            emailError,
            telefonoError,
            passwordError,
            rolError,
            departamentoError
        ).all { it == null }

        if (formularioValido) {
            onDatosValidos(
                CrearCuentaDTO(
                    nombre = nombre,
                    apellidos = apellidos,
                    email = email,
                    telefono = telefono,
                    password = currentState.password,
                    rol = rol,
                    departamento = departamento
                )
            )
        }
    }
}