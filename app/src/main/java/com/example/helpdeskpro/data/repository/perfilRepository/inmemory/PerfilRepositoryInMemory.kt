package com.example.helpdeskpro.data.repository.perfilRepository.inmemory

import com.example.helpdeskpro.data.repository.inmemoryRepository.InMemoryHelpdeskStore
import com.example.helpdeskpro.data.repository.perfilRepository.PerfilRepository
import com.example.helpdeskpro.modelo.EditarPerfilDTO
import com.example.helpdeskpro.modelo.PerfilDTO

class PerfilRepositoryInMemory(
    private val store: InMemoryHelpdeskStore
) : PerfilRepository {
    override suspend fun obtenerPerfil(userId: String): PerfilDTO {
        val id = userId.toLongOrNull()
            ?: throw IllegalArgumentException("El identificador del usuario no es válido.")
        return store.usuarios[id]?.perfil
            ?: throw NoSuchElementException("No se encontró el perfil solicitado.")
    }

    override suspend fun actualizarPerfil(datos: EditarPerfilDTO): PerfilDTO {
        val usuario = store.usuarios[datos.id]
            ?: throw NoSuchElementException("No se encontró el perfil solicitado.")
        if (store.usuarios.values.any {
                it.perfil.id != datos.id &&
                    it.perfil.email.equals(datos.email.trim(), ignoreCase = true)
            }
        ) {
            throw IllegalArgumentException("Ya existe una cuenta con ese correo.")
        }
        val perfil = usuario.perfil.copy(
            nombre = datos.nombre.trim(),
            apellidos = datos.apellidos.trim(),
            email = datos.email.trim(),
            telefono = datos.telefono.trim()
        )
        usuario.perfil = perfil
        usuario.admin = usuario.admin.copy(
            nombre = perfil.nombre,
            apellidos = perfil.apellidos,
            email = perfil.email,
            telefono = perfil.telefono
        )
        datos.contrasena?.takeIf { it.isNotBlank() }?.let { usuario.password = it }
        return perfil
    }
}
