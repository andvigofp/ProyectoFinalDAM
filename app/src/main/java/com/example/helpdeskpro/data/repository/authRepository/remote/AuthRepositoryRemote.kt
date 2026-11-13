package com.example.helpdeskpro.data.repository.authRepository.remote

import com.example.helpdeskpro.data.remote.HelpdeskApi
import com.example.helpdeskpro.data.remote.ApiException
import com.example.helpdeskpro.data.remote.LoginRequest
import com.example.helpdeskpro.data.remote.PasswordRecoveryRequest
import com.example.helpdeskpro.data.remote.RegisterRequest
import com.example.helpdeskpro.data.remote.ResetPasswordRequest
import com.example.helpdeskpro.data.remote.SessionTokenStore
import com.example.helpdeskpro.data.remote.apiCall
import com.example.helpdeskpro.data.remote.catalogKey
import com.example.helpdeskpro.data.remote.toRoleCode
import com.example.helpdeskpro.modelo.CrearCuentaDTO
import com.example.helpdeskpro.modelo.LoginDTO
import com.example.helpdeskpro.modelo.RecuperarContrasenaDTO
import com.example.helpdeskpro.modelo.RestablecerContrasenaDTO
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
                rol = response.usuario.rolNombre.toRoleCode(),
                nombre = response.usuario.nombre
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
        SesionUsuarioDTO(
            userId = response.id.toString(),
            rol = response.rolNombre.toRoleCode(),
            nombre = datos.nombre
        )
    }

    override suspend fun recuperarContrasena(solicitud: RecuperarContrasenaDTO) {
        apiCall {
            api.requestPasswordRecovery(PasswordRecoveryRequest(solicitud.email))
        }
    }

    override suspend fun restablecerContrasena(solicitud: RestablecerContrasenaDTO) {
        apiCall {
            api.resetPassword(
                ResetPasswordRequest(
                    email = solicitud.email,
                    codigo = solicitud.codigo,
                    nuevaContrasena = solicitud.nuevaContrasena
                )
            )
        }
    }
}
