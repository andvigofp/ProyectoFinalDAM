package com.example.helpdeskpro.viemodel.ustate

import com.example.helpdeskpro.modelo.HomeAdminDTO

data class HomeAdminUiState(
    val resumen: HomeAdminDTO? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
