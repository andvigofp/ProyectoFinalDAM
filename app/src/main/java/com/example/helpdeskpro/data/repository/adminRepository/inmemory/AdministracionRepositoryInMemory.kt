package com.example.helpdeskpro.data.repository.adminRepository.inmemory

import com.example.helpdeskpro.data.repository.adminRepository.AdministracionRepository
import com.example.helpdeskpro.data.repository.inmemoryRepository.InMemoryHelpdeskStore
import com.example.helpdeskpro.modelo.AdminUsuarioDTO
import com.example.helpdeskpro.modelo.HomeAdminDTO
import com.example.helpdeskpro.modelo.PerfilDTO
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdministracionRepositoryInMemory(
    private val store: InMemoryHelpdeskStore
) : AdministracionRepository {
    override suspend fun obtenerResumen(): HomeAdminDTO {
        val estados = store.incidencias.values.map { it.detalle.estado.uppercase(Locale.ROOT) }
        return HomeAdminDTO(
            abiertas = estados.count { it == "ABIERTA" },
            enProgreso = estados.count { it == "EN PROGRESO" || it == "EN_PROGRESO" },
            resueltas = estados.count { it == "RESUELTA" }
        )
    }

    override suspend fun obtenerUsuarios(): List<AdminUsuarioDTO> =
        store.usuarios.values.map { it.admin }

    override fun siguienteUsuarioId(): Long = store.siguienteUsuarioId()

    override suspend fun guardarUsuario(
        usuario: AdminUsuarioDTO,
        password: String?
    ): AdminUsuarioDTO {
        if (store.usuarios.values.any {
                it.perfil.id != usuario.id &&
                    it.perfil.email.equals(usuario.email.trim(), ignoreCase = true)
            }
        ) {
            throw IllegalArgumentException("Ya existe una cuenta con ese correo.")
        }
        val existing = store.usuarios[usuario.id]
        if (existing == null) {
            val id = if (usuario.id > 0) usuario.id else store.siguienteUsuarioId()
            val profile = PerfilDTO(
                id = id,
                nombre = usuario.nombre.trim(),
                apellidos = usuario.apellidos.trim(),
                email = usuario.email.trim(),
                telefono = usuario.telefono.trim(),
                departamento = usuario.departamento,
                miembroDesde = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
                rol = usuario.rol
            )
            store.usuarios[id] = InMemoryHelpdeskStore.Usuario(
                perfil = profile,
                admin = usuario.copy(id = id),
                password = password.orEmpty()
            )
            return usuario.copy(id = id)
        } else {
            val profile = existing.perfil.copy(
                nombre = usuario.nombre.trim(),
                apellidos = usuario.apellidos.trim(),
                email = usuario.email.trim(),
                telefono = usuario.telefono.trim(),
                departamento = usuario.departamento,
                rol = usuario.rol
            )
            existing.perfil = profile
            existing.admin = usuario.copy(id = usuario.id)
            password?.takeIf { it.isNotBlank() }?.let { existing.password = it }
            return usuario
        }
    }
}
