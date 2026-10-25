package com.example.helpdeskpro.navehost

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * Eventos globales de navegación y sesión.
 * Permite reaccionar a navegación externa o a una sesión no autorizada.
 */
object SessionEvents {

    private val _navigation = MutableSharedFlow<RouteNavigation>()
    val navigation: SharedFlow<RouteNavigation> = _navigation

    private val _forbidden = MutableSharedFlow<Unit>()
    val forbidden: SharedFlow<Unit> = _forbidden

    suspend fun emitNavigation(route: RouteNavigation) {
        _navigation.emit(route)
    }

    suspend fun emitForbidden() {
        _forbidden.emit(Unit)
    }
}
