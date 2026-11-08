package com.example.helpdeskpro.modelo

/** Identidad y rol asignados después de autenticar una cuenta. */
data class SesionUsuarioDTO(
    val userId: String,
    val rol: String
)
