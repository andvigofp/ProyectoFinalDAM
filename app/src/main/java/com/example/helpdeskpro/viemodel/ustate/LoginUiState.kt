package com.example.helpdeskpro.viemodel.ustate

/**
 * Estado único de la pantalla de login.
 *
 * @param email correo electrónico introducido.
 * @param password contraseña introducida.
 * @param emailError error de validación del correo.
 * @param passwordError error de validación de la contraseña.
 * @param isLoading indica si hay una operación en curso.
 * @param errorMessage error general del inicio de sesión.
 * @param loginSuccess indica si el inicio de sesión terminó correctamente.
 * @param userId identificador del usuario autenticado, si existe.
 */
data class LoginUiState(
    val email: String="",
    val password: String="",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val loginSuccess: Boolean = false,
    val userId: String? = null,
    val userRole: String? = null,
    val userName: String? = null
)
