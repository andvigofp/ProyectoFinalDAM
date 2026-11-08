package com.example.helpdeskpro.data.repository.adminRepository

import com.example.helpdeskpro.modelo.AdminUsuarioDTO
import com.example.helpdeskpro.modelo.HomeAdminDTO

interface AdministracionRepository {
    suspend fun obtenerResumen(): HomeAdminDTO
    suspend fun obtenerUsuarios(): List<AdminUsuarioDTO>
    suspend fun guardarUsuario(usuario: AdminUsuarioDTO, password: String? = null): AdminUsuarioDTO
    fun siguienteUsuarioId(): Long
}
