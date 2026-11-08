package com.example.helpdeskpro.data.remote

import org.junit.Assert.assertEquals
import org.junit.Test

class ApiMappersTest {
    @Test
    fun normalizaRolesYEstadosDeLaApiAlModeloDeLaApp() {
        assertEquals("TECNICO", "Técnico".toRoleCode())
        assertEquals("ADMINISTRADOR", "Administrador".toRoleCode())
        assertEquals("EMPLEADO", "Empleado".toRoleCode())
        assertEquals("EN PROGRESO", "En proceso".toStateCode())
        assertEquals("RESUELTA", "Resuelta".toStateCode())
    }

    @Test
    fun convierteFechasIsoAFormatoDePantalla() {
        assertEquals(
            "08/11/2026",
            IncidentResponse(
                id = 1L,
                titulo = "Incidencia",
                descripcion = "Descripción",
                solucion = null,
                fechaCreacion = "2026-11-08T12:30:00",
                usuarioId = 1L,
                usuarioNombre = "Ana Pérez",
                departamentoNombre = "Informática",
                tecnicoId = null,
                tecnicoNombre = null,
                categoriaId = 1L,
                categoriaNombre = "Hardware",
                prioridadId = 2L,
                prioridadNombre = "Media",
                estadoId = 1L,
                estadoNombre = "Abierta"
            ).toIncidentList().fecha
        )
    }
}
