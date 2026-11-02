package com.example.helpdeskpro.viemodel.ustate

/**
 * Estado único de la pantalla de recuperación de contraseña.
 *
 * @param email correo electrónico introducido.
 * @param emailError error de validación del correo.
 * @param isLoading indica si hay una solicitud en curso.
 * @param errorMessage error general de la solicitud.
 * @param successMessage confirmación de una solicitud correcta.
 */
data class RecuperarContrasenaUiState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
