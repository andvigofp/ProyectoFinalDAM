package com.example.helpdeskpro.data.remote

import com.example.helpdeskpro.modelo.AdminUsuarioDTO
import com.example.helpdeskpro.modelo.ComentarioIncidenciaDTO
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
