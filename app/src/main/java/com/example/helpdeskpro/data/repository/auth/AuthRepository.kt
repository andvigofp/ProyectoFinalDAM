package com.example.helpdeskpro.data.repository.auth

import com.example.helpdeskpro.modelo.CrearCuentaDTO
import com.example.helpdeskpro.modelo.LoginDTO
import com.example.helpdeskpro.modelo.RecuperarContrasenaDTO
import com.example.helpdeskpro.modelo.SesionUsuarioDTO

interface AuthRepository {
    suspend fun iniciarSesion(credenciales: LoginDTO): SesionUsuarioDTO
    suspend fun crearCuenta(datos: CrearCuentaDTO): SesionUsuarioDTO
    suspend fun recuperarContrasena(solicitud: RecuperarContrasenaDTO)
}
