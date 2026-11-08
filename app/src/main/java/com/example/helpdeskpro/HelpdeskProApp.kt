package com.example.helpdeskpro

import android.app.Application
import com.example.helpdeskpro.data.remote.HelpdeskApiClient
import com.example.helpdeskpro.data.repository.adminRepository.AdministracionRepository
import com.example.helpdeskpro.data.repository.adminRepository.remote.AdministracionRepositoryRemote
import com.example.helpdeskpro.data.repository.authRepository.AuthRepository
import com.example.helpdeskpro.data.repository.authRepository.remote.AuthRepositoryRemote
import com.example.helpdeskpro.data.repository.incidenciasRepository.IncidenciasRepository
import com.example.helpdeskpro.data.repository.incidenciasRepository.remote.IncidenciasRepositoryRemote
import com.example.helpdeskpro.data.repository.perfilRepository.PerfilRepository
import com.example.helpdeskpro.data.repository.perfilRepository.remote.PerfilRepositoryRemote

/** Aplicación que comparte la sesión y los repositorios REST. */
class HelpdeskProApp : Application() {
    private val apiClient by lazy { HelpdeskApiClient(this) }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryRemote(apiClient.api, apiClient.tokenStore)
    }

    val incidenciasRepository: IncidenciasRepository by lazy {
        IncidenciasRepositoryRemote(apiClient.api)
    }

    val perfilRepository: PerfilRepository by lazy {
        PerfilRepositoryRemote(apiClient.api)
    }

    val administracionRepository: AdministracionRepository by lazy {
        AdministracionRepositoryRemote(apiClient.api)
    }

    suspend fun cerrarSesion() = apiClient.tokenStore.clear()
}
