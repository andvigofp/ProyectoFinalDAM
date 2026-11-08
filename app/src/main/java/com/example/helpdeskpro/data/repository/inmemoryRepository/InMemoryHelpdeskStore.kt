package com.example.helpdeskpro.data.repository.inmemoryRepository

import com.example.helpdeskpro.modelo.AdminUsuarioDTO
import com.example.helpdeskpro.modelo.ComentarioIncidenciaDTO
import com.example.helpdeskpro.modelo.DetalleIncidenciaTecnicoDTO
import com.example.helpdeskpro.modelo.HistorialIncidenciaDTO
import com.example.helpdeskpro.modelo.PerfilDTO

/**
 * Almacén compartido para que las implementaciones en memoria mantengan
 * coherentes los usuarios, perfiles e incidencias durante la ejecución.
 */
class InMemoryHelpdeskStore {
    data class Usuario(
        var perfil: PerfilDTO,
        var admin: AdminUsuarioDTO,
        var password: String
    )

    data class Incidencia(
        var detalle: DetalleIncidenciaTecnicoDTO,
        val creadorId: Long
    )

    val usuarios = linkedMapOf<Long, Usuario>()
    val incidencias = linkedMapOf<Long, Incidencia>()

    init {
        agregarUsuario(
            id = 1L,
            nombre = "Ana",
            apellidos = "Pérez",
            email = "ana@empresa.com",
            telefono = "600 123 456",
            rol = "EMPLEADO",
            departamento = "Ventas",
            password = "123456",
            miembroDesde = "01/09/2026",
            activo = true
        )
        agregarUsuario(
            id = 2L,
            nombre = "Luis",
            apellidos = "Gómez",
            email = "luis@empresa.com",
            telefono = "600 234 567",
            rol = "TECNICO",
            departamento = "Sistemas",
            password = "123456",
            miembroDesde = "01/09/2026",
            activo = true
        )
        agregarUsuario(
            id = 3L,
            nombre = "Marta",
            apellidos = "Díaz",
            email = "marta@empresa.com",
            telefono = "600 345 678",
            rol = "ADMINISTRADOR",
            departamento = "Dirección",
            password = "123456",
            miembroDesde = "01/09/2026",
            activo = true
        )
        agregarUsuario(
            id = 4L,
            nombre = "Pedro",
            apellidos = "Sanz",
            email = "pedro@empresa.com",
            telefono = "600 456 789",
            rol = "EMPLEADO",
            departamento = "RRHH",
            password = "123456",
            miembroDesde = "01/09/2026",
            activo = false
        )

        agregarIncidencia(
            DetalleIncidenciaTecnicoDTO(
                id = 1L,
                titulo = "No enciende el ordenador",
                descripcion = "El ordenador no arranca al pulsar el botón de encendido.",
                estado = "ABIERTA",
                creador = "Ana Pérez",
                departamento = "Ventas",
                categoria = "Hardware",
                prioridad = "Alta",
                fechaCreacion = "15/10/2026"
            ),
            creadorId = 1L
        )
        agregarIncidencia(
            DetalleIncidenciaTecnicoDTO(
                id = 2L,
                titulo = "VPN no conecta en casa",
                descripcion = "No se puede establecer la conexión VPN desde casa.",
                estado = "EN PROGRESO",
                creador = "Carlos Ruiz",
                departamento = "Sistemas",
                categoria = "Red",
                prioridad = "Media",
                fechaCreacion = "16/10/2026"
            ),
            creadorId = 2L
        )
    }

    fun agregarUsuario(
        id: Long,
        nombre: String,
        apellidos: String,
        email: String,
        telefono: String,
        rol: String,
        departamento: String,
        password: String,
        miembroDesde: String,
        activo: Boolean
    ) {
        val perfil = PerfilDTO(
            id = id,
            nombre = nombre,
            apellidos = apellidos,
            email = email,
            telefono = telefono,
            departamento = departamento,
            miembroDesde = miembroDesde,
            rol = rol
        )
        val admin = AdminUsuarioDTO(
            id = id,
            nombre = nombre,
            apellidos = apellidos,
            email = email,
            telefono = telefono,
            rol = rol,
            departamento = departamento,
            activo = activo
        )
        usuarios[id] = Usuario(perfil, admin, password)
    }

    fun agregarIncidencia(detalle: DetalleIncidenciaTecnicoDTO, creadorId: Long) {
        incidencias[detalle.id] = Incidencia(detalle, creadorId)
    }

    fun siguienteUsuarioId(): Long = (usuarios.keys.maxOrNull() ?: 0L) + 1L

    fun siguienteIncidenciaId(): Long = (incidencias.keys.maxOrNull() ?: 0L) + 1L

    fun agregarComentario(id: Long, comentario: ComentarioIncidenciaDTO) {
        val incidencia = incidencias[id]
            ?: throw NoSuchElementException("No se encontró la incidencia.")
        val detalle = incidencia.detalle
        incidencia.detalle = detalle.copy(comentarios = detalle.comentarios + comentario)
    }

    fun agregarHistorial(id: Long, descripcion: String) {
        val incidencia = incidencias[id]
            ?: throw NoSuchElementException("No se encontró la incidencia.")
        val detalle = incidencia.detalle
        incidencia.detalle = detalle.copy(
            historial = detalle.historial + HistorialIncidenciaDTO(
                fecha = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                    .format(java.util.Date()),
                descripcion = descripcion
            )
        )
    }
}
