package com.example.helpdeskpro.viemodel.ustate

import com.example.helpdeskpro.modelo.PerfilDTO

data class EditarPerfilUiState(
    val perfil: PerfilDTO? = null,
    val contrasena: String = "",
    val nombreError: String? = null,
    val apellidosError: String? = null,
    val emailError: String? = null,
    val telefonoError: String? = null,
    val contrasenaError: String? = null,
    val errorMessage: String? = null,
    val isLoading: Boolean = false
)
