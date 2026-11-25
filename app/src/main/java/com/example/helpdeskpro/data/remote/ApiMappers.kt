package com.example.helpdeskpro.data.remote

import com.example.helpdeskpro.modelo.AdminUsuarioDTO
import com.example.helpdeskpro.modelo.ComentarioIncidenciaDTO
import com.example.helpdeskpro.modelo.DetalleIncidenciaEmpleadoDTO
import com.example.helpdeskpro.modelo.DetalleIncidenciaTecnicoDTO
import com.example.helpdeskpro.modelo.HistorialIncidenciaDTO
import com.example.helpdeskpro.modelo.HomeAdminDTO
import com.example.helpdeskpro.modelo.HomeTecnicoDTO
import com.example.helpdeskpro.modelo.IncidenciaDTO
import com.example.helpdeskpro.modelo.PerfilDTO
import java.text.Normalizer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal fun UserResponse.toAdminUsuario() = AdminUsuarioDTO(
    id = id,
    nombre = nombre,
    apellidos = apellidos,
    email = email,
    telefono = telefono.orEmpty(),
    rol = rolNombre.toRoleCode(),
    departamento = departamentoNombre.orEmpty(),
    activo = activo ?: false
)

internal fun UserResponse.toPerfil() = PerfilDTO(
    id = id,
    nombre = nombre,
    apellidos = apellidos,
    email = email,
    telefono = telefono.orEmpty(),
    departamento = departamentoNombre.orEmpty(),
    miembroDesde = fechaRegistro?.toDisplayDate().orEmpty(),
    rol = rolNombre.toRoleCode()
)

internal fun IncidentResponse.toIncidentList() = IncidenciaDTO(
    id = id,
    titulo = titulo,
    categoria = categoriaNombre,
    prioridad = prioridadNombre,
    fecha = fechaCreacion.toDisplayDate(),
    estado = estadoNombre.toStateCode()
)

internal fun IncidentResponse.toTechnicianList() = HomeTecnicoDTO(
    id = id,
    titulo = titulo,
    categoria = categoriaNombre,
    prioridad = prioridadNombre,
    fecha = fechaCreacion.toDisplayDate(),
    estado = estadoNombre.toStateCode(),
    creador = usuarioNombre
)

internal fun IncidentResponse.toDetail(
    comments: List<CommentResponse>,
    history: List<HistoryResponse>
) = DetalleIncidenciaTecnicoDTO(
    id = id,
    titulo = titulo,
    descripcion = descripcion,
    estado = estadoNombre.toStateCode(),
    creador = usuarioNombre,
    departamento = departamentoNombre.orEmpty(),
    categoria = categoriaNombre,
    prioridad = prioridadNombre,
    fechaCreacion = fechaCreacion.toDisplayDate(),
    tecnico = tecnicoNombre.orEmpty(),
    comentarios = comments.map {
        ComentarioIncidenciaDTO(autor = it.usuarioNombre, texto = it.mensaje)
    },
    historial = history.map {
        HistorialIncidenciaDTO(
            fecha = it.fecha.toDisplayDate(),
            descripcion = it.accion
        )
    }
)

/**
 * Convierte la respuesta de la API en el detalle que ve el empleado.
 * Los comentarios propios se muestran como "Yo" y los del técnico asignado como "Técnico".
 * El historial siempre muestra la creación, el técnico asignado y el estado actual,
 * aunque en la tabla de historiales falte alguno de esos movimientos.
 *
 * @param comments comentarios de la incidencia.
 * @param history historial de la incidencia.
 * @param userId identificador del empleado autenticado.
 */
internal fun IncidentResponse.toEmployeeDetail(
    comments: List<CommentResponse>,
    history: List<HistoryResponse>,
    userId: Long? = null
) = DetalleIncidenciaEmpleadoDTO(
    id = id,
    titulo = titulo,
    descripcion = descripcion,
    estado = estadoNombre.toStateCode(),
    comentarios = comments.map { comment ->
        ComentarioIncidenciaDTO(
            autor = when {
                userId != null && comment.usuarioId == userId -> "Yo"
                tecnicoId != null && comment.usuarioId == tecnicoId -> "Técnico"
                else -> comment.usuarioNombre
            },
            texto = comment.mensaje
        )
    },
    historial = toEmployeeHistory(history)
)

/**
 * Construye el historial del empleado a partir del historial de la API,
 * completándolo con la creación, la asignación y el estado actual de la incidencia.
 */
private fun IncidentResponse.toEmployeeHistory(
    history: List<HistoryResponse>
): List<HistorialIncidenciaDTO> {
    val historial = history.map { entry ->
        HistorialIncidenciaDTO(
            fecha = entry.fecha.toShortDate(),
            descripcion = entry.toEmployeeText(tecnicoNombre)
        )
    }.toMutableList()

    val acciones = history.map { it.accion.trim().lowercase() }

    // Creación de la incidencia
    if (acciones.none { it.startsWith("incidencia creada") }) {
        historial.add(
            0,
            HistorialIncidenciaDTO(
                fecha = fechaCreacion.toShortDate(),
                descripcion = "Creada por $usuarioNombre (ABIERTA)"
            )
        )
    }

    // Técnico asignado
    if (!tecnicoNombre.isNullOrBlank() &&
        acciones.none { it.startsWith("incidencia asignada") }
    ) {
        historial.add(
            1,
            HistorialIncidenciaDTO(
                fecha = fechaCreacion.toShortDate(),
                descripcion = "Asignada a $tecnicoNombre"
            )
        )
    }

    // Estado actual: siempre se muestra como última línea si no está ya registrado
    val estadoActual = estadoNombre.toStateCode()
    val ultimoEstado = historial.lastOrNull { it.descripcion.startsWith("Estado → ") }
        ?.descripcion?.removePrefix("Estado → ")
    if (ultimoEstado != estadoActual) {
        historial.add(
            HistorialIncidenciaDTO(
                fecha = (fechaActualizacion ?: fechaCreacion).toShortDate(),
                descripcion = "Estado → $estadoActual"
            )
        )
    }

    return historial
}

/** Resume el texto del historial que guarda la API para mostrarlo al empleado. */
private fun HistoryResponse.toEmployeeText(tecnicoNombre: String?): String {
    val texto = accion.trim()
    return when {
        texto.equals("Incidencia creada", ignoreCase = true) ->
            usuarioNombre?.let { "Creada por $it (ABIERTA)" } ?: "Creada (ABIERTA)"
        texto.startsWith("Incidencia asignada a ", ignoreCase = true) ->
            "Asignada a " + texto.substring("Incidencia asignada a ".length)
        texto.startsWith("Incidencia asignada", ignoreCase = true) ->
            tecnicoNombre?.let { "Asignada a $it" } ?: "Asignada a un técnico"
        texto.startsWith("Estado actualizado a ", ignoreCase = true) ->
            "Estado → " + texto.substring("Estado actualizado a ".length).toStateCode()
        else -> texto
    }
}

internal fun List<IncidentResponse>.toAdminSummary(): HomeAdminDTO {
    val states = map { it.estadoNombre.toStateCode() }
    return HomeAdminDTO(
        abiertas = states.count { it == "ABIERTA" },
        enProgreso = states.count { it == "EN PROGRESO" },
        resueltas = states.count { it == "RESUELTA" }
    )
}

internal fun String.toRoleCode(): String = when (normalized()) {
    "administrador", "admin" -> "ADMINISTRADOR"
    "tecnico", "tecnica" -> "TECNICO"
    else -> "EMPLEADO"
}

internal fun String.toStateCode(): String = when (normalized()) {
    "abierta", "abierto" -> "ABIERTA"
    "en proceso", "en progreso" -> "EN PROGRESO"
    "resuelta", "resuelto" -> "RESUELTA"
    "cerrada", "cerrado" -> "CERRADA"
    else -> uppercase()
}

internal fun String.catalogKey(): String = normalized()

private fun String.normalized(): String = Normalizer
    .normalize(trim(), Normalizer.Form.NFD)
    .replace(Regex("\\p{Mn}+"), "")
    .lowercase()

/** Fecha corta dd/MM para el historial. */
private fun String?.toShortDate(): String = toDisplayDate().substringBeforeLast('/')

private fun String?.toDisplayDate(): String {
    if (this.isNullOrBlank()) return ""
    val datePart = substringBefore('T')
    return runCatching {
        LocalDateTime.parse(this).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }.getOrElse {
        runCatching {
            java.time.LocalDate.parse(datePart).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        }.getOrDefault(this)
    }
}
