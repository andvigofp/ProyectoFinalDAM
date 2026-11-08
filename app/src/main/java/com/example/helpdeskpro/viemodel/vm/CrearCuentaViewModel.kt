package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.helpdeskpro.data.repository.auth.AuthRepository
import com.example.helpdeskpro.data.repository.auth.inmemory.AuthRepositoryInMemory
import com.example.helpdeskpro.data.repository.inmemory.InMemoryHelpdeskStore
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.CrearCuentaDTO
import com.example.helpdeskpro.viemodel.ustate.CrearCuentaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

/**
 * Mantiene el formulario y valida los datos de registro de una cuenta.
 *
 * @property uiState flujo observable del estado del formulario.
 * @param application aplicación Android usada para acceder a recursos.
 */
class CrearCuentaViewModel(
    application: Application,
    private val authRepository: AuthRepository = AuthRepositoryInMemory(InMemoryHelpdeskStore())
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(CrearCuentaUiState())
    val uiState: StateFlow<CrearCuentaUiState> = _uiState.asStateFlow()

    /**
     * Actualiza el nombre y limpia su error de validación.
     *
     * @param nombre nombre introducido.
     */
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

    /**
     * Actualiza los apellidos y limpia su error de validación.
     *
     * @param apellidos apellidos introducidos.
     */
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

    /**
     * Actualiza el email y limpia su error de validación.
     *
     * @param email correo introducido.
     */
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

    /**
     * Actualiza el teléfono y limpia su error de validación.
     *
     * @param telefono teléfono introducido.
     */
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
                cuentaCreada = false
            )
        }
    }

    /**
     * Actualiza el rol y limpia su error de validación.
     *
     * @param rol rol seleccionado.
     */
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

    /**
     * Actualiza el departamento y limpia su error de validación.
     *
     * @param departamento departamento seleccionado.
     */
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
     * Valida el formulario y crea la cuenta mediante el repositorio.
     */
    fun crearCuenta() {

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
            val datos = CrearCuentaDTO(
                    nombre = nombre,
                    apellidos = apellidos,
                    email = email,
                    telefono = telefono,
                    password = currentState.password,
                    rol = rol,
                    departamento = departamento
                )
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                try {
                    authRepository.crearCuenta(datos)
                    _uiState.update {
                        it.copy(isLoading = false, cuentaCreada = true, errorMessage = null)
                    }
                } catch (exception: CancellationException) {
                    throw exception
                } catch (exception: Exception) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message
                                ?: getApplication<Application>().getString(R.string.error_create_account)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Crea instancias de [CrearCuentaViewModel].
 *
 * @param application aplicación Android asociada al ViewModel.
 */
class CrearCuentaViewModelFactory(
    private val application: Application,
    private val authRepository: AuthRepository = AuthRepositoryInMemory(InMemoryHelpdeskStore())
) : ViewModelProvider.Factory {

    /**
     * Crea el ViewModel solicitado o falla si el tipo no está soportado.
     *
     * @param modelClass clase del ViewModel que se debe crear.
     * @return instancia del tipo solicitado.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CrearCuentaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CrearCuentaViewModel(application, authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}