package com.example.helpdeskpro.navehost

import kotlinx.serialization.Serializable

/**
 * Rutas tipadas de navegación de HelpDesk Pro.
 * Las nuevas páginas se añadirán aquí como nuevos destinos.
 */
sealed interface RouteNavigation {

    @Serializable object Login : RouteNavigation
    @Serializable object RecuperarContrasena : RouteNavigation
    @Serializable object CrearCuenta : RouteNavigation

    @Serializable
    class HomeEmpleado(val userId: String) : RouteNavigation

    @Serializable
    class NuevaIncidencia(val userId: String) : RouteNavigation

    @Serializable
    class Perfil(val userId: String) : RouteNavigation

}
