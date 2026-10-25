package com.example.helpdeskpro.viemodel.vm

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.helpdeskpro.modelo.CrearCuentaDTO
import com.example.helpdeskpro.viemodel.ustate.CrearCuentaUiState

class CrearCuentaViewModel : ViewModel() {

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

        val nombreError = if (nombre.isEmpty()) "Introduce tu nombre" else null
        val apellidosError = if (apellidos.isEmpty()) "Introduce tus apellidos" else null
        val emailError = when {
            email.isEmpty() -> "Introduce tu email"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Introduce un email válido"
            else -> null
        }
        val telefonoError = if (telefono.isEmpty()) "Introduce tu teléfono" else null
        val passwordError = when {
            currentState.password.isBlank() -> "Introduce una contraseña"
            currentState.password.length < 6 ->
                "La contraseña debe tener al menos 6 caracteres"
            else -> null
        }
        val rolError = if (rol.isEmpty()) "Selecciona un rol" else null
        val departamentoError = if (departamento.isEmpty()) {
            "Selecciona un departamento"
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
