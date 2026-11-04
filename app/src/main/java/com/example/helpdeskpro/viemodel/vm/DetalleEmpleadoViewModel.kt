package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.ComentarioIncidenciaDTO
import com.example.helpdeskpro.modelo.DetalleIncidenciaTecnicoDTO
import com.example.helpdeskpro.viemodel.ustate.DetalleEmpleadoUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Mantiene los datos y las acciones de la pantalla de detalle de incidencia.
 *
 * @property uiState flujo observable del estado de la pantalla.
 * @param application aplicación Android usada para obtener recursos.
 * @param userId identificador del usuario autenticado.
 * @param incidenciaId identificador de la incidencia mostrada.
 */
class DetalleEmpleadoViewModel(
    application: Application,
    val userId: String,
    val incidenciaId: Long
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(DetalleEmpleadoUiState())
    val uiState: StateFlow<DetalleEmpleadoUiState> = _uiState.asStateFlow()

    /**
     * Actualiza el estado seleccionado en la pantalla.
     *
     * @param estado nuevo estado seleccionado.
     */
    fun onEstadoChange(estado: String) {
        _uiState.update {
            it.copy(estadoSeleccionado = estado)
        }
    }

    /**
     * Actualiza el texto de la solución.
     *
     * @param solucion texto escrito por el usuario.
     */
    fun onSolucionChange(solucion: String) {
        _uiState.update {
            it.copy(solucion = solucion)
        }
    }

    /**
     * Entrega el estado seleccionado si no está vacío.
     *
     * @param onEstadoValido acción que recibe el estado válido.
     */
    fun guardarEstado(onEstadoValido: (String) -> Unit = {}) {
        val estado = _uiState.value.estadoSeleccionado
        if (estado.isNotBlank()) {
            onEstadoValido(estado)
        }
    }

    /**
     * Entrega la solución recortada si contiene texto.
     *
     * @param onSolucionValida acción que recibe la solución válida.
     */
    fun registrarSolucion(onSolucionValida: (String) -> Unit = {}) {
        val solucion = _uiState.value.solucion.trim()
        if (solucion.isNotEmpty()) {
            onSolucionValida(solucion)
        }
    }

    /**
     * Actualiza el comentario y elimina sus errores anteriores.
     *
     * @param comentario nuevo texto del comentario.
     */
    fun onComentarioChange(comentario: String) {
        _uiState.update {
            it.copy(
                comentario = comentario,
                comentarioError = null,
                errorMessage = null
            )
        }
    }

    /**
     * Carga el detalle de la incidencia.
     * La llamada al repositorio/API se añadirá en la iteración de integración.
     *
     * @param cargar función suspendida que obtiene el detalle.
     */
    fun cargarDetalle(cargar: suspend () -> DetalleIncidenciaTecnicoDTO) {

        if (_uiState.value.isLoading) return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val detalle = cargar()

                _uiState.update {
                    it.copy(
                        detalle = detalle,
                        estadoSeleccionado = "",
                        isLoading = false,
                        errorMessage = null
                    )
                }

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message
                            ?: getApplication<Application>()
                                .getString(R.string.error_load_incident_detail)
                    )
                }
            }
        }
    }

    /**
     * Actualiza los datos mostrados, por ejemplo en preview o tras una edición.
     *
     * @param detalle nuevos datos de la incidencia.
     */
    fun establecerDetalle(detalle: DetalleIncidenciaTecnicoDTO) {
        _uiState.update {
            it.copy(
                detalle = detalle,
                estadoSeleccionado = "",
                errorMessage = null
            )
        }
    }

    /**
     * Valida el comentario y lo añade al hilo local.
     * La llamada al repositorio/API se añadirá en la iteración de integración.
     *
     * @param onComentarioValido acción opcional que recibe el comentario añadido.
     */
    fun enviarComentario(
        onComentarioValido: (ComentarioIncidenciaDTO) -> Unit = {}
    ) {
        val texto = _uiState.value.comentario.trim()
        val detalleActual = _uiState.value.detalle

        val comentarioError = if (texto.isEmpty()) {
            getApplication<Application>().getString(R.string.error_comment_required)
        } else {
            null
        }

        _uiState.update {
            it.copy(comentarioError = comentarioError)
        }

        if (comentarioError != null || detalleActual == null) return

        val comentario = ComentarioIncidenciaDTO(
            autor = getApplication<Application>().getString(R.string.comment_author_me),
            texto = texto
        )

        _uiState.update {
            it.copy(
                comentario = "",
                comentarioError = null,
                detalle = detalleActual.copy(
                    comentarios = detalleActual.comentarios + comentario
                )
            )
        }

        onComentarioValido(comentario)
    }

    /** Elimina el mensaje de error actual. */
    fun limpiarError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}

/**
 * Crea instancias de [DetalleEmpleadoViewModel] con los identificadores de usuario e incidencia.
 *
 * @param application aplicación Android asociada al ViewModel.
 * @param userId identificador del usuario autenticado.
 * @param incidenciaId identificador de la incidencia seleccionada.
 */
class DetalleEmpleadoViewModelFactory(
    private val application: Application,
    private val userId: String,
    private val incidenciaId: Long
) : ViewModelProvider.Factory {

    /**
     * Crea el ViewModel solicitado o falla si el tipo no está soportado.
     *
     * @param modelClass clase del ViewModel que se debe crear.
     * @return instancia del tipo solicitado.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleEmpleadoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetalleEmpleadoViewModel(
                application = application,
                userId = userId,
                incidenciaId = incidenciaId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
