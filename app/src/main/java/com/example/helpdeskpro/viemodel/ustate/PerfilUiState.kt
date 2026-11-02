package com.example.helpdeskpro.viemodel.ustate

import com.example.helpdeskpro.modelo.PerfilDTO

/**
 * Estado único de la pantalla del perfil del usuario.
 *
 * @param perfil datos del perfil cargado.
 * @param isLoading indica si se está cargando o actualizando el perfil.
 * @param errorMessage error producido al cargar o actualizar el perfil.
 */
data class PerfilUiState(
    val perfil: PerfilDTO? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)