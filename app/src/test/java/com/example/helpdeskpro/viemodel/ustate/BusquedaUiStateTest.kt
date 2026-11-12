package com.example.helpdeskpro.viemodel.ustate

import com.example.helpdeskpro.modelo.AdminUsuarioDTO
import com.example.helpdeskpro.modelo.HomeTecnicoDTO
import com.example.helpdeskpro.modelo.IncidenciaDTO
import org.junit.Assert.assertEquals
import org.junit.Test

class BusquedaUiStateTest {

    @Test
    fun usuariosFiltradosMatchNameAndSortAlphabetically() {
        val state = AdminUsuariosUiState(
            usuarios = listOf(
                usuario(1, "Zara", "López"),
                usuario(2, "Ana", "Pérez"),
                usuario(3, "Marta", "Díaz")
            ),
            searchQuery = "a"
        )

        assertEquals(
            listOf("Ana Pérez", "Marta Díaz", "Zara López"),
            state.usuariosFiltrados.map { "${it.nombre} ${it.apellidos}" }
        )
    }

    @Test
    fun employeeIncidentsMatchNamesIgnoringAccentsAndSortAlphabetically() {
        val state = HomeEmpleadoUiState(
            incidencias = listOf(
                incidencia(1, "VPN no conecta en casa"),
                incidencia(2, "No enciende el ordenador"),
                incidencia(3, "Fallo de conexión")
            ),
            searchQuery = "conexion"
        )

        assertEquals(
            listOf("Fallo de conexión"),
            state.incidenciasFiltradas.map { it.titulo }
        )
    }

    @Test
    fun technicianSearchKeepsStatusFilter() {
        val state = HomeTecnicoUiState(
            incidencias = listOf(
                HomeTecnicoDTO(1, "VPN no conecta", "", "", "", "ABIERTA", ""),
                HomeTecnicoDTO(2, "VPN no conecta en casa", "", "", "", "RESUELTA", ""),
                HomeTecnicoDTO(3, "No inicia el equipo", "", "", "", "ABIERTA", "")
            ),
            filtroEstado = "ABIERTA",
            searchQuery = "vpn"
        )

        assertEquals(
            listOf("VPN no conecta"),
            state.incidenciasFiltradas.map { it.titulo }
        )
    }

    private fun usuario(id: Long, nombre: String, apellidos: String) = AdminUsuarioDTO(
        id = id,
        nombre = nombre,
        apellidos = apellidos,
        email = "",
        telefono = "",
        rol = "EMPLEADO",
        departamento = "",
        activo = true
    )

    private fun incidencia(id: Long, titulo: String) = IncidenciaDTO(
        id = id,
        titulo = titulo,
        categoria = "",
        prioridad = "",
        fecha = "",
        estado = "ABIERTA"
    )
}
