package com.example.helpdeskpro.data.repository.incidencias

import com.example.helpdeskpro.modelo.ComentarioIncidenciaDTO
import com.example.helpdeskpro.modelo.DetalleIncidenciaTecnicoDTO
import com.example.helpdeskpro.modelo.HomeTecnicoDTO
import com.example.helpdeskpro.modelo.IncidenciaDTO
import com.example.helpdeskpro.modelo.NuevaIncidenciaDTO

interface IncidenciasRepository {
    suspend fun obtenerIncidenciasEmpleado(userId: String): List<IncidenciaDTO>
    suspend fun obtenerIncidenciasTecnico(userId: String): List<HomeTecnicoDTO>
    suspend fun obtenerDetalle(incidenciaId: Long): DetalleIncidenciaTecnicoDTO
    suspend fun crearIncidencia(userId: String, datos: NuevaIncidenciaDTO): Long
    suspend fun actualizarEstado(incidenciaId: Long, estado: String)
    suspend fun registrarSolucion(incidenciaId: Long, solucion: String)
    suspend fun agregarComentario(incidenciaId: Long, comentario: ComentarioIncidenciaDTO)
}
