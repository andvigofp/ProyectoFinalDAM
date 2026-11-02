package com.example.helpdeskpro.navehost

import kotlinx.serialization.Serializable

/**
 * Rutas tipadas de navegación de HelpDesk Pro.
 * Las nuevas páginas se añadirán aquí como nuevos destinos.
 */
sealed interface RouteNavigation {

    /** Destino de inicio de sesión. */
    @Serializable object Login : RouteNavigation
    /** Destino para solicitar la recuperación de contraseña. */
    @Serializable object RecuperarContrasena : RouteNavigation
    /** Destino para registrar una cuenta. */
    @Serializable object CrearCuenta : RouteNavigation

    /**
     * Destino de la página principal del empleado.
     *
     * @param userId identificador del usuario autenticado.
     */
    @Serializable
    class HomeEmpleado(val userId: String) : RouteNavigation

    /**
     * Destino del formulario para crear una incidencia.
     *
     * @param userId identificador del usuario que crea la incidencia.
     */
    @Serializable
    class NuevaIncidencia(val userId: String) : RouteNavigation

    /**
     * Destino del detalle de una incidencia seleccionada.
     *
     * @param userId identificador del usuario autenticado.
     * @param incidenciaId identificador de la incidencia.
     * @param titulo título de la incidencia.
     * @param categoria categoría de la incidencia.
     * @param prioridad prioridad de la incidencia.
     * @param fecha fecha de creación de la incidencia.
     * @param estado estado actual de la incidencia.
     */
    @Serializable
    class DetalleIncidenciaTecnico(
        val userId: String,
        val incidenciaId: Long,
        val titulo: String,
        val categoria: String,
        val prioridad: String,
        val fecha: String,
        val estado: String
    ) : RouteNavigation

    /**
     * Destino del perfil del usuario.
     *
     * @param userId identificador del usuario cuyo perfil se muestra.
     */
    @Serializable
    class Perfil(val userId: String) : RouteNavigation

}
