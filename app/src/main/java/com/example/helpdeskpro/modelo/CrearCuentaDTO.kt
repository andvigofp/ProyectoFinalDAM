package com.example.helpdeskpro.modelo

/**
 * Datos necesarios para registrar una nueva cuenta de usuario.
 */
data class CrearCuentaDTO(
    val nombre: String,
    val apellidos: String,
    val email: String,
    val telefono: String,
    val password: String,
    val rol: String,
    val departamento: String
)
