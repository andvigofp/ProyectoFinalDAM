package com.example.helpdeskpro.viemodel.ustate

data class RestablecerContrasenaUiState(
    val email: String = "",
    val codigo: String = "",
    val nuevaContrasena: String = "",
    val emailError: String? = null,
    val codigoError: String? = null,
    val contrasenaError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)
