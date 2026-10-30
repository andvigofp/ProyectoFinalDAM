package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.CrearCuentaDTO
import com.example.helpdeskpro.viemodel.ustate.CrearCuentaUiState

class CrearCuentaViewModel(application: Application) : AndroidViewModel(application) {

    var uiState by mutableStateOf(CrearCuentaUiState())
        private set

    fun onNombreChange(nombre: String) {
        uiState = uiState.copy(
            nombre = nombre,
            nombreError = null,
            errorMessage = null,
            cuentaCreada = false
        )
    }

    fun onApellidosChange(apellidos: String) {
        uiState = uiState.copy(
            apellidos = apellidos,
            apellidosError = null,
            errorMessage = null,
            cuentaCreada = false
        )
    }

    fun onEmailChange(email: String) {
        uiState = uiState.copy(
            email = email,
            emailError = null,
            errorMessage = null,
            cuentaCreada = false
        )
    }

    fun onTelefonoChange(telefono: String) {
        uiState = uiState.copy(
            telefono = telefono,
            telefonoError = null,
            errorMessage = null,
            cuentaCreada = false
        )
    }

    fun onPasswordChange(password: String) {
        uiState = uiState.copy(
            password = password,
            passwordError = null,
            errorMessage = null,
            cuentaCreada = false
        )
    }

    fun onRolChange(rol: String) {
        uiState = uiState.copy(
            rol = rol,
            rolError = null,
            errorMessage = null,
            cuentaCreada = false
        )
    }

    fun onDepartamentoChange(departamento: String) {
        uiState = uiState.copy(
            departamento = departamento,
            departamentoError = null,
            errorMessage = null,
            cuentaCreada = false
        )
    }

    /**
     * Valida el formulario y entrega el DTO cuando todos los datos son correctos.
     * La llamada al repositorio/API se añadirá en la iteración de integración.
     */
    fun crearCuenta(onDatosValidos: (CrearCuentaDTO) -> Unit = {}) {
        val currentState = uiState
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

        uiState = currentState.copy(
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
