package com.example.helpdeskpro.viemodel.ustate

/**
 * Estado único de la pantalla de recuperación de contraseña.
 */
data class RecuperarContrasenaUiState(
    val email: String = "",
    val emailError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
