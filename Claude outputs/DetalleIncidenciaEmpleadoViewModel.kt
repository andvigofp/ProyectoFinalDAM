package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.helpdeskpro.R
import com.example.helpdeskpro.data.repository.incidenciasRepository.IncidenciasRepository
import com.example.helpdeskpro.data.repository.incidenciasRepository.inmemory.IncidenciasRepositoryInMemory
import com.example.helpdeskpro.data.repository.inmemoryRepository.InMemoryHelpdeskStore
import com.example.helpdeskpro.modelo.ComentarioIncidenciaDTO
import com.example.helpdeskpro.viemodel.ustate.DetalleIncidenciaEmpleadoUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Mantiene los datos y las acciones de la pantalla de detalle de incidencia del empleado.
 *
 * @property uiState flujo observable del estado de la pantalla.
 * @param application aplicación Android usada para obtener recursos.
 * @param userId identificador del empleado autenticado.
 * @param incidenciaId identificador de la incidencia mostrada.
 */
class DetalleIncidenciaEmpleadoViewModel(
    application: Application,
    val userId: String,
    val incidenciaId: Long,
    private val incidenciasRepository: IncidenciasRepository =
        IncidenciasRepositoryInMemory(InMemoryHelpdeskStore())
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(DetalleIncidenciaEmpleadoUiState())
    val uiState: StateFlow<DetalleIncidenciaEmpleadoUiState> = _uiState.asStateFlow()

    /**
     * Actualiza el comentario y elimina sus errores anteriores.
     *
     * @param comentario nuevo texto del comentario.
     */
    fun onComentarioChange(comentario: String) {
        _uiState.update {
            it.copy(comentario = comentario, comentarioError = null, errorMessage = null)
        }
    }

    /**
     * Carga el detalle de la incidencia desde el repositorio.
     */
    fun cargarDetalle() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val detalle = incidenciasRepository.obtenerDetalleEmpleado(incidenciaId)
                _uiState.update {
                    it.copy(detalle = detalle, isLoading = false, errorMessage = null)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = exception.message
                            ?: getApplication<Application>().getString(
                                R.string.error_load_incident_detail
                            )
                    )
                }
            }
        }
    }

    /**
     * Valida el comentario y lo guarda mediante el repositorio.
     * Después recarga el detalle para mostrar el comentario y el historial actualizados.
     */
    fun enviarComentario() {
        val state = _uiState.value
        if (state.isSending) return

        val texto = state.comentario.trim()
        if (texto.isEmpty()) {
            _uiState.update {
                it.copy(comentarioError = getApplication<Application>().getString(
                    R.string.error_comment_required
                ))
            }
            return
        }
        if (state.detalle == null) return

        val comentario = ComentarioIncidenciaDTO(
            autor = getApplication<Application>().getString(R.string.comment_author_me),
            texto = texto
        )
        viewModelScope.launch {
            _uiState.update { it.copy(isSending = true, errorMessage = null) }
            try {
                incidenciasRepository.agregarComentario(
                    incidenciaId = incidenciaId,
                    comentario = comentario,
                    userId = userId
                )
                val detalle = incidenciasRepository.obtenerDetalleEmpleado(incidenciaId)
                _uiState.update {
                    it.copy(
                        detalle = detalle,
                        comentario = "",
                        comentarioError = null,
                        isSending = false,
                        errorMessage = null
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        isSending = false,
                        errorMessage = exception.message
                            ?: getApplication<Application>().getString(R.string.error_send_comment)
                    )
                }
            }
        }
    }
}

/**
 * Crea instancias de [DetalleIncidenciaEmpleadoViewModel] con los identificadores de usuario e incidencia.
 *
 * @param application aplicación Android asociada al ViewModel.
 * @param userId identificador del empleado autenticado.
 * @param incidenciaId identificador de la incidencia seleccionada.
 */
class DetalleIncidenciaEmpleadoViewModelFactory(
    private val application: Application,
    private val userId: String,
    private val incidenciaId: Long,
    private val incidenciasRepository: IncidenciasRepository =
        IncidenciasRepositoryInMemory(InMemoryHelpdeskStore())
) : ViewModelProvider.Factory {
    /**
     * Crea el ViewModel solicitado o falla si el tipo no está soportado.
     *
     * @param modelClass clase del ViewModel que se debe crear.
     * @return instancia del tipo solicitado.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleIncidenciaEmpleadoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetalleIncidenciaEmpleadoViewModel(
                application = application,
                userId = userId,
                incidenciaId = incidenciaId,
                incidenciasRepository = incidenciasRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
