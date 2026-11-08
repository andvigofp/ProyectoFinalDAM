package com.example.helpdeskpro.data.repository.adminRepository.remote

import com.example.helpdeskpro.data.remote.ApiException
import com.example.helpdeskpro.data.remote.HelpdeskApi
import com.example.helpdeskpro.data.remote.UserRequest
import com.example.helpdeskpro.data.remote.apiCall
import com.example.helpdeskpro.data.remote.catalogKey
import com.example.helpdeskpro.data.remote.toAdminUsuario
import com.example.helpdeskpro.data.remote.toAdminSummary
import com.example.helpdeskpro.modelo.AdminUsuarioDTO
import com.example.helpdeskpro.modelo.HomeAdminDTO

class AdministracionRepositoryRemote(
    private val api: HelpdeskApi
) : com.example.helpdeskpro.data.repository.adminRepository.AdministracionRepository {
    override suspend fun obtenerResumen(): HomeAdminDTO = apiCall {
        api.incidents().toAdminSummary()
    }

    override suspend fun obtenerUsuarios(): List<AdminUsuarioDTO> =
        apiCall { api.users().map { it.toAdminUsuario() } }

    override fun siguienteUsuarioId(): Long = 0L

    override suspend fun guardarUsuario(
        usuario: AdminUsuarioDTO,
        password: String?
    ): AdminUsuarioDTO = apiCall {
        val roles = api.catalog("roles")
        val departments = api.catalog("departamentos")
        val role = roles.firstOrNull { it.nombre.catalogKey() == usuario.rol.catalogKey() }
            ?: throw ApiException("El rol seleccionado ya no existe en el servidor.")
        val departmentId = departments.firstOrNull {
            it.nombre.catalogKey() == usuario.departamento.catalogKey()
        }?.id ?: if (usuario.departamento.isBlank()) null else {
            throw ApiException("El departamento seleccionado ya no existe en el servidor.")
        }
        val request = UserRequest(
            nombre = usuario.nombre,
            apellidos = usuario.apellidos,
            email = usuario.email,
            password = password,
            telefono = usuario.telefono,
            activo = usuario.activo,
            rolId = role.id,
            departamentoId = departmentId
        )
        val response = if (usuario.id <= 0L) {
            if (password.isNullOrBlank()) {
                throw ApiException("La contraseña es obligatoria para crear un usuario.")
            }
            api.createUser(request)
        } else {
            api.updateUser(usuario.id, request)
        }
        response.toAdminUsuario()
    }
}
