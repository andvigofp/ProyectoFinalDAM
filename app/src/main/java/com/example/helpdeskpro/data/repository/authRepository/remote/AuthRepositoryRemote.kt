package com.example.helpdeskpro.data.repository.authRepository.remote

import com.example.helpdeskpro.data.remote.ApiException
import com.example.helpdeskpro.data.remote.HelpdeskApi
import com.example.helpdeskpro.data.remote.LoginRequest
import com.example.helpdeskpro.data.remote.RegisterRequest
import com.example.helpdeskpro.data.remote.SessionTokenStore
import com.example.helpdeskpro.data.remote.apiCall
import com.example.helpdeskpro.data.remote.catalogKey
import com.example.helpdeskpro.data.remote.toRoleCode
import com.example.helpdeskpro.modelo.CrearCuentaDTO
import com.example.helpdeskpro.modelo.LoginDTO
import com.example.helpdeskpro.modelo.RecuperarContrasenaDTO
import com.example.helpdeskpro.modelo.SesionUsuarioDTO

class AuthRepositoryRemote(
    private val api: HelpdeskApi,
    private val tokenStore: SessionTokenStore
) : com.example.helpdeskpro.data.repository.authRepository.AuthRepository {
    override suspend fun iniciarSesion(credenciales: LoginDTO): SesionUsuarioDTO =
        apiCall {
            val response = api.login(LoginRequest(credenciales.email, credenciales.password))
            tokenStore.save(response.token)
            SesionUsuarioDTO(
                userId = response.usuario.id.toString(),
                rol = response.usuario.rolNombre.toRoleCode()
            )
        }

    override suspend fun crearCuenta(datos: CrearCuentaDTO): SesionUsuarioDTO = apiCall {
        val departmentId = api.catalog("departamentos")
            .firstOrNull { it.nombre.catalogKey() == datos.departamento.catalogKey() }
            ?.id
            ?: throw ApiException("El departamento seleccionado ya no existe en el servidor.")
        val response = api.register(
            RegisterRequest(
                nombre = datos.nombre,
                apellidos = datos.apellidos,
                email = datos.email,
                password = datos.password,
                telefono = datos.telefono,
                departamentoId = departmentId
            )
        )
        SesionUsuarioDTO(response.id.toString(), response.rolNombre.toRoleCode())
    }

    override suspend fun recuperarContrasena(solicitud: RecuperarContrasenaDTO) {
        throw ApiException(
            "La recuperación por correo aún no está disponible: el servidor no tiene un servicio de envío de email configurado."
        )
    }
}
