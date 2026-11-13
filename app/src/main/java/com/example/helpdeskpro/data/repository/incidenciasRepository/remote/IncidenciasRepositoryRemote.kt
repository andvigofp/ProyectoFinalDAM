package com.example.helpdeskpro.data.repository.incidenciasRepository.remote

import com.example.helpdeskpro.data.remote.CommentRequest
import com.example.helpdeskpro.data.remote.HelpdeskApi
import com.example.helpdeskpro.data.remote.IncidentRequest
import com.example.helpdeskpro.data.remote.apiCall
import com.example.helpdeskpro.data.remote.catalogKey
import com.example.helpdeskpro.data.remote.toDetail
import com.example.helpdeskpro.data.remote.toIncidentList
import com.example.helpdeskpro.data.remote.toRoleCode
import com.example.helpdeskpro.data.remote.toStateCode
import com.example.helpdeskpro.data.remote.toTechnicianList
import com.example.helpdeskpro.modelo.ComentarioIncidenciaDTO
import com.example.helpdeskpro.modelo.DetalleIncidenciaTecnicoDTO
import com.example.helpdeskpro.modelo.HomeTecnicoDTO
import com.example.helpdeskpro.modelo.IncidenciaDTO
import com.example.helpdeskpro.modelo.NuevaIncidenciaDTO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class IncidenciasRepositoryRemote(
    private val api: HelpdeskApi
) : com.example.helpdeskpro.data.repository.incidenciasRepository.IncidenciasRepository {
    override suspend fun obtenerIncidenciasEmpleado(
        userId: String,
        busqueda: String
    ): List<IncidenciaDTO> =
        apiCall {
            val employeeId = userId.toLongOrNull()
                ?: throw IllegalArgumentException("El identificador del empleado no es válido.")
            api.incidentsForUser(employeeId, busqueda).map { it.toIncidentList() }
        }

    override suspend fun obtenerIncidenciasTecnico(
        userId: String,
        busqueda: String
    ): List<HomeTecnicoDTO> =
        apiCall {
            val technicianId = userId.toLongOrNull()
                ?: throw IllegalArgumentException("El identificador del técnico no es válido.")
            val currentUser = api.currentUser()
            val incidents = if (currentUser.rolNombre.toRoleCode() == "ADMINISTRADOR") {
                api.incidents(busqueda)
            } else {
                if (currentUser.id != technicianId) {
                    throw IllegalArgumentException(
                        "El identificador no corresponde al técnico autenticado."
                    )
                }
                api.technicianIncidents(technicianId, busqueda)
            }
            incidents.map { it.toTechnicianList() }
        }

    override suspend fun obtenerDetalle(incidenciaId: Long): DetalleIncidenciaTecnicoDTO =
        apiCall {
            coroutineScope {
                val detail = async { api.incident(incidenciaId) }
                val comments = async { api.comments(incidenciaId) }
                val history = async { api.history(incidenciaId) }
                detail.await().toDetail(comments.await(), history.await())
            }
        }

    override suspend fun crearIncidencia(userId: String, datos: NuevaIncidenciaDTO): Long =
        apiCall {
            val categories = api.catalog("categorias")
            val priorities = api.catalog("prioridades")
            val category = categories.firstOrNull {
                it.nombre.catalogKey() == datos.categoria.catalogKey()
            } ?: throw IllegalArgumentException("La categoría seleccionada no existe en el servidor.")
            val priority = priorities.firstOrNull {
                it.nombre.catalogKey() == datos.prioridad.catalogKey()
            } ?: throw IllegalArgumentException("La prioridad seleccionada no existe en el servidor.")
            api.createIncident(
                IncidentRequest(
                    titulo = datos.titulo,
                    descripcion = datos.descripcion,
                    usuarioId = userId.toLongOrNull(),
                    categoriaId = category.id,
                    prioridadId = priority.id
                )
            ).id
        }

    override suspend fun actualizarEstado(incidenciaId: Long, estado: String) {
        apiCall {
            val normalizedState = estado.toStateCode()
            val state = api.catalog("estados").firstOrNull {
                it.nombre.toStateCode() == normalizedState
            } ?: throw IllegalArgumentException("El estado seleccionado no existe en el servidor.")
            api.updateIncidentState(incidenciaId, state.id)
        }
    }

    override suspend fun registrarSolucion(incidenciaId: Long, solucion: String) {
        apiCall {
            api.registerSolution(
                incidenciaId,
                IncidentRequest(solucion = solucion)
            )
        }
    }

    override suspend fun agregarComentario(
        incidenciaId: Long,
        comentario: ComentarioIncidenciaDTO
    ) {
        apiCall {
            api.addComment(
                CommentRequest(
                    mensaje = comentario.texto,
                    usuarioId = null,
                    incidenciaId = incidenciaId
                )
            )
        }
    }
}
