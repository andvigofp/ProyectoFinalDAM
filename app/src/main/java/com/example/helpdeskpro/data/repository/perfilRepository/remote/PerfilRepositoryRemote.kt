package com.example.helpdeskpro.data.repository.perfilRepository.remote

import com.example.helpdeskpro.data.remote.HelpdeskApi
import com.example.helpdeskpro.data.remote.ProfileRequest
import com.example.helpdeskpro.data.remote.apiCall
import com.example.helpdeskpro.data.remote.toPerfil
import com.example.helpdeskpro.modelo.EditarPerfilDTO
import com.example.helpdeskpro.modelo.PerfilDTO

class PerfilRepositoryRemote(
    private val api: HelpdeskApi
) : com.example.helpdeskpro.data.repository.perfilRepository.PerfilRepository {
    override suspend fun obtenerPerfil(userId: String): PerfilDTO = apiCall {
        val user = api.currentUser()
        if (user.id.toString() != userId) {
            throw IllegalArgumentException("El perfil solicitado no corresponde al usuario autenticado.")
        }
        user.toPerfil()
    }

    override suspend fun actualizarPerfil(datos: EditarPerfilDTO): PerfilDTO = apiCall {
        val updated = api.updateCurrentUser(
            ProfileRequest(
                nombre = datos.nombre,
                apellidos = datos.apellidos,
                email = datos.email,
                telefono = datos.telefono,
                password = datos.contrasena
            )
        )
        if (updated.id != datos.id) {
            throw IllegalArgumentException("El servidor devolvió un perfil distinto al solicitado.")
        }
        updated.toPerfil()
    }
}
