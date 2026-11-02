package com.example.helpdeskpro.navehost

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

/**
 * Eventos globales de navegación y sesión.
 * Permite reaccionar a navegación externa o a una sesión no autorizada.
 *
 * @property navigation flujo de destinos de navegación solicitados.
 * @property forbidden flujo de eventos de sesión no autorizada.
 */
object SessionEvents {

    private val _navigation = MutableSharedFlow<RouteNavigation>()
    val navigation: SharedFlow<RouteNavigation> = _navigation

    private val _forbidden = MutableSharedFlow<Unit>()
    val forbidden: SharedFlow<Unit> = _forbidden

    /**
     * Publica una solicitud global de navegación.
     *
     * @param route destino al que debe navegar la aplicación.
     */
    suspend fun emitNavigation(route: RouteNavigation) {
        _navigation.emit(route)
    }

    /** Publica un evento que indica que la sesión no está autorizada. */
    suspend fun emitForbidden() {
        _forbidden.emit(Unit)
    }
}
