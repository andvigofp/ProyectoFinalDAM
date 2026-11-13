package com.example.helpdeskpro.data.repository.authRepository.inmemory

import com.example.helpdeskpro.data.repository.authRepository.AuthRepository
import com.example.helpdeskpro.data.repository.inmemoryRepository.InMemoryHelpdeskStore
import com.example.helpdeskpro.modelo.CrearCuentaDTO
import com.example.helpdeskpro.modelo.LoginDTO
import com.example.helpdeskpro.modelo.RecuperarContrasenaDTO
import com.example.helpdeskpro.modelo.RestablecerContrasenaDTO
import com.example.helpdeskpro.modelo.SesionUsuarioDTO
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AuthRepositoryInMemory(
    private val store: InMemoryHelpdeskStore
) : AuthRepository {
    override suspend fun iniciarSesion(credenciales: LoginDTO): SesionUsuarioDTO {
        val usuario = store.usuarios.values.firstOrNull {
            it.perfil.email.equals(credenciales.email.trim(), ignoreCase = true) &&
                it.password == credenciales.password &&
                it.admin.activo
        } ?: throw IllegalArgumentException("El correo o la contraseña no son correctos.")

        return SesionUsuarioDTO(
            userId = usuario.perfil.id.toString(),
            rol = usuario.perfil.rol,
            nombre = usuario.perfil.nombre
        )
    }

    override suspend fun crearCuenta(datos: CrearCuentaDTO): SesionUsuarioDTO {
        if (store.usuarios.values.any {
                it.perfil.email.equals(datos.email.trim(), ignoreCase = true)
            }
        ) {
            throw IllegalArgumentException("Ya existe una cuenta con ese correo.")
        }

        val id = store.siguienteUsuarioId()
        store.agregarUsuario(
            id = id,
            nombre = datos.nombre.trim(),
            apellidos = datos.apellidos.trim(),
            email = datos.email.trim(),
            telefono = datos.telefono.trim(),
            rol = datos.rol.trim().uppercase(Locale.ROOT),
            departamento = datos.departamento.trim(),
            password = datos.password,
            miembroDesde = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
            activo = true
        )
        return SesionUsuarioDTO(
            userId = id.toString(),
            rol = datos.rol.trim().uppercase(Locale.ROOT),
            nombre = datos.nombre.trim()
        )
    }

    override suspend fun recuperarContrasena(solicitud: RecuperarContrasenaDTO) {
        if (store.usuarios.values.none {
                it.perfil.email.equals(solicitud.email.trim(), ignoreCase = true) &&
                    it.admin.activo
            }
        ) {
            throw IllegalArgumentException("No existe una cuenta activa con ese correo.")
        }
    }

    override suspend fun restablecerContrasena(solicitud: RestablecerContrasenaDTO) {
        throw IllegalStateException(
            "El restablecimiento de contraseña requiere la conexión con el servidor."
        )
    }
}
