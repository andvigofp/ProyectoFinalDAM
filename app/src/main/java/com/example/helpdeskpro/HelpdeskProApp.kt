package com.example.helpdeskpro

import android.app.Application
import com.example.helpdeskpro.data.repository.adminRepository.AdministracionRepository
import com.example.helpdeskpro.data.repository.adminRepository.inmemory.AdministracionRepositoryInMemory
import com.example.helpdeskpro.data.repository.authRepository.AuthRepository
import com.example.helpdeskpro.data.repository.authRepository.inmemory.AuthRepositoryInMemory
import com.example.helpdeskpro.data.repository.incidenciasRepository.IncidenciasRepository
import com.example.helpdeskpro.data.repository.incidenciasRepository.inmemory.IncidenciasRepositoryInMemory
import com.example.helpdeskpro.data.repository.inmemoryRepository.InMemoryHelpdeskStore
import com.example.helpdeskpro.data.repository.perfilRepository.PerfilRepository
import com.example.helpdeskpro.data.repository.perfilRepository.inmemory.PerfilRepositoryInMemory

/** Aplicación que mantiene las dependencias en memoria durante la sesión. */
class HelpdeskProApp : Application() {
    val inMemoryStore by lazy { InMemoryHelpdeskStore() }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryInMemory(inMemoryStore)
    }

    val incidenciasRepository: IncidenciasRepository by lazy {
        IncidenciasRepositoryInMemory(inMemoryStore)
    }

    val perfilRepository: PerfilRepository by lazy {
        PerfilRepositoryInMemory(inMemoryStore)
    }

    val administracionRepository: AdministracionRepository by lazy {
        AdministracionRepositoryInMemory(inMemoryStore)
    }
}
