package com.example.helpdeskpro.viemodel.ustate

import com.example.helpdeskpro.modelo.PerfilDTO

/**
 * Estado único de la pantalla del perfil del usuario.
 */
data class PerfilUiState(
    val perfil: PerfilDTO? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
