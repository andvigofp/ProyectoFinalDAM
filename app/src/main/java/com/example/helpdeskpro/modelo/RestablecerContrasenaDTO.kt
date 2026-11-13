package com.example.helpdeskpro.modelo

data class RestablecerContrasenaDTO(
    val email: String,
    val codigo: String,
    val nuevaContrasena: String
)
