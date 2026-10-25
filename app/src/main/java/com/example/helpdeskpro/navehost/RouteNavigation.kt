package com.example.helpdeskpro.navehost

import kotlinx.serialization.Serializable

/**
 * Rutas tipadas de navegación de HelpDesk Pro.
 * Las nuevas páginas se añadirán aquí como nuevos destinos.
 */
sealed interface RouteNavigation {

    @Serializable
    data object Login : RouteNavigation

    @Serializable
    data object RecuperarContrasena : RouteNavigation

    @Serializable
    data object CrearCuenta : RouteNavigation
}
