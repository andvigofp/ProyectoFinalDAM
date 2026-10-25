package com.example.helpdeskpro.viemodel.ustate

/**
 * Estado único de la pantalla de registro de una nueva cuenta.
 */
data class CrearCuentaUiState(
    val nombre: String = "",
    val apellidos: String = "",
    val email: String = "",
    val telefono: String = "",
    val password: String = "",
    val rol: String = "",
    val departamento: String = "",
    val nombreError: String? = null,
    val apellidosError: String? = null,
    val emailError: String? = null,
    val telefonoError: String? = null,
    val passwordError: String? = null,
    val rolError: String? = null,
    val departamentoError: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val cuentaCreada: Boolean = false
)
