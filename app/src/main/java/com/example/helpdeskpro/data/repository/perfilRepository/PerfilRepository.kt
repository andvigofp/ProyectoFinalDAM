package com.example.helpdeskpro.data.repository.perfilRepository

import com.example.helpdeskpro.modelo.EditarPerfilDTO
import com.example.helpdeskpro.modelo.PerfilDTO

interface PerfilRepository {
    suspend fun obtenerPerfil(userId: String): PerfilDTO
    suspend fun actualizarPerfil(datos: EditarPerfilDTO): PerfilDTO
}
