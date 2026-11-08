package com.example.helpdeskpro.data.repository.incidencias.inmemory

import com.example.helpdeskpro.data.repository.incidencias.IncidenciasRepository
import com.example.helpdeskpro.data.repository.inmemory.InMemoryHelpdeskStore
import com.example.helpdeskpro.modelo.ComentarioIncidenciaDTO
import com.example.helpdeskpro.modelo.DetalleIncidenciaTecnicoDTO
import com.example.helpdeskpro.modelo.HomeTecnicoDTO
import com.example.helpdeskpro.modelo.IncidenciaDTO
import com.example.helpdeskpro.modelo.NuevaIncidenciaDTO
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class IncidenciasRepositoryInMemory(
    private val store: InMemoryHelpdeskStore
) : IncidenciasRepository {
    override suspend fun obtenerIncidenciasEmpleado(userId: String): List<IncidenciaDTO> {
        val id = userId.toLongOrNull() ?: return emptyList()
        return store.incidencias.values
            .filter { it.creadorId == id }
            .map { incidencia ->
                incidencia.detalle.toIncidenciaDTO()
            }
    }

    override suspend fun obtenerIncidenciasTecnico(userId: String): List<HomeTecnicoDTO> =
        store.incidencias.values.map { incidencia ->
            incidencia.detalle.toHomeTecnicoDTO()
        }

    override suspend fun obtenerDetalle(incidenciaId: Long): DetalleIncidenciaTecnicoDTO =
        store.incidencias[incidenciaId]?.detalle
            ?: throw NoSuchElementException("No se encontró la incidencia solicitada.")

    override suspend fun crearIncidencia(userId: String, datos: NuevaIncidenciaDTO): Long {
        val creadorId = userId.toLongOrNull()
            ?: throw IllegalArgumentException("El identificador del usuario no es válido.")
        val creador = store.usuarios[creadorId]?.perfil
            ?: throw NoSuchElementException("No se encontró el usuario que crea la incidencia.")
        val id = store.siguienteIncidenciaId()
        val detalle = DetalleIncidenciaTecnicoDTO(
            id = id,
            titulo = datos.titulo.trim(),
            descripcion = datos.descripcion.trim(),
            estado = "ABIERTA",
            creador = "${creador.nombre} ${creador.apellidos}".trim(),
            departamento = creador.departamento,
            categoria = datos.categoria.trim(),
            prioridad = datos.prioridad.trim(),
            fechaCreacion = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        )
        store.agregarIncidencia(detalle, creadorId)
        store.agregarHistorial(id, "Incidencia creada.")
        return id
    }

    override suspend fun actualizarEstado(incidenciaId: Long, estado: String) {
        val incidencia = store.incidencias[incidenciaId]
            ?: throw NoSuchElementException("No se encontró la incidencia solicitada.")
        val nuevoEstado = estado.trim().uppercase(Locale.ROOT)
        incidencia.detalle = incidencia.detalle.copy(estado = nuevoEstado)
        store.agregarHistorial(incidenciaId, "Estado actualizado a $nuevoEstado.")
    }

    override suspend fun registrarSolucion(incidenciaId: Long, solucion: String) {
        val incidencia = store.incidencias[incidenciaId]
            ?: throw NoSuchElementException("No se encontró la incidencia solicitada.")
        incidencia.detalle = incidencia.detalle.copy(estado = "RESUELTA")
        store.agregarHistorial(incidenciaId, "Solución registrada: ${solucion.trim()}")
    }

    override suspend fun agregarComentario(
        incidenciaId: Long,
        comentario: ComentarioIncidenciaDTO
    ) {
        store.agregarComentario(incidenciaId, comentario)
    }

    private fun DetalleIncidenciaTecnicoDTO.toIncidenciaDTO() = IncidenciaDTO(
        id = id,
        titulo = titulo,
        categoria = categoria,
        prioridad = prioridad,
        fecha = fechaCreacion,
        estado = estado
    )

    private fun DetalleIncidenciaTecnicoDTO.toHomeTecnicoDTO() = HomeTecnicoDTO(
        id = id,
        titulo = titulo,
        categoria = categoria,
        prioridad = prioridad,
        fecha = fechaCreacion,
        estado = estado,
        creador = creador
    )
}
