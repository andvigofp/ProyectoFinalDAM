package com.example.helpdeskpro.viemodel.ustate

import com.example.helpdeskpro.modelo.AdminUsuarioDTO

data class AdminUsuariosUiState(
    val usuarios: List<AdminUsuarioDTO> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    val usuariosFiltrados: List<AdminUsuarioDTO>
        get() = usuarios
}
