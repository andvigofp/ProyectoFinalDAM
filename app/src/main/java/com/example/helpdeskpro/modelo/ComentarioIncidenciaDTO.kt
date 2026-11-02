package com.example.helpdeskpro.modelo

/**
 * Comentario publicado en el hilo de una incidencia.
 *
 * @param autor nombre de quien escribió el comentario.
 * @param texto contenido del comentario.
 */
data class ComentarioIncidenciaDTO(
    val autor: String,
    val texto: String
)
