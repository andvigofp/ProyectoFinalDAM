package com.example.helpdeskpro.data.repository.inmemory

import com.example.helpdeskpro.data.repository.admin.inmemory.AdministracionRepositoryInMemory
import com.example.helpdeskpro.data.repository.auth.inmemory.AuthRepositoryInMemory
import com.example.helpdeskpro.data.repository.incidencias.inmemory.IncidenciasRepositoryInMemory
import com.example.helpdeskpro.data.repository.perfil.inmemory.PerfilRepositoryInMemory
import com.example.helpdeskpro.modelo.EditarPerfilDTO
import com.example.helpdeskpro.modelo.LoginDTO
import com.example.helpdeskpro.modelo.NuevaIncidenciaDTO
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class InMemoryRepositoriesTest {
    @Test
    fun autenticaCuentasActivasYRechazaLasInactivas() {
        runBlocking {
            val repository = AuthRepositoryInMemory(InMemoryHelpdeskStore())

            assertEquals(
                "1",
                repository.iniciarSesion(LoginDTO("ana@empresa.com", "123456")).userId
            )
            val cuentaInactivaRechazada = try {
                repository.iniciarSesion(LoginDTO("pedro@empresa.com", "123456"))
                false
            } catch (_: IllegalArgumentException) {
                true
            }
            assertTrue(cuentaInactivaRechazada)
        }
    }

    @Test
    fun crearIncidenciaActualizaListasYResumenAdmin() {
        runBlocking {
            val store = InMemoryHelpdeskStore()
            val incidencias = IncidenciasRepositoryInMemory(store)
            val admin = AdministracionRepositoryInMemory(store)

            val id = incidencias.crearIncidencia(
                userId = "1",
                datos = NuevaIncidenciaDTO(
                    titulo = "No funciona el correo",
                    descripcion = "No puedo enviar mensajes.",
                    categoria = "Software",
                    prioridad = "Media"
                )
            )

            assertEquals(2, incidencias.obtenerIncidenciasEmpleado("1").size)
            assertEquals(2, admin.obtenerResumen().abiertas)

            incidencias.actualizarEstado(id, "RESUELTA")

            assertEquals("RESUELTA", incidencias.obtenerDetalle(id).estado)
            assertEquals(1, admin.obtenerResumen().resueltas)
        }
    }

    @Test
    fun actualizarPerfilSincronizaLosDatosDeAutenticacion() {
        runBlocking {
            val store = InMemoryHelpdeskStore()
            val perfiles = PerfilRepositoryInMemory(store)
            val auth = AuthRepositoryInMemory(store)

            perfiles.actualizarPerfil(
                EditarPerfilDTO(
                    id = 1L,
                    nombre = "Ana María",
                    apellidos = "Pérez",
                    email = "ana.nueva@empresa.com",
                    telefono = "600 123 456",
                    contrasena = "nueva123"
                )
            )

            assertEquals("Ana María", perfiles.obtenerPerfil("1").nombre)
            assertEquals(
                "1",
                auth.iniciarSesion(LoginDTO("ana.nueva@empresa.com", "nueva123")).userId
            )
        }
    }
}
