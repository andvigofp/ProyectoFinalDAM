package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.ComentarioIncidenciaDTO
import com.example.helpdeskpro.modelo.DetalleIncidenciaDTO
import com.example.helpdeskpro.viemodel.ustate.DetalleEmpleadoUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetalleEmpleadoViewModel(
    application: Application,
    val userId: String,
    val incidenciaId: Long
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(DetalleEmpleadoUiState())
    val uiState: StateFlow<DetalleEmpleadoUiState> = _uiState.asStateFlow()

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
     */
    fun cargarDetalle(cargar: suspend () -> DetalleIncidenciaDTO) {

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

    /** Actualiza los datos mostrados, por ejemplo en preview o tras una edición. */
    fun establecerDetalle(detalle: DetalleIncidenciaDTO) {
        _uiState.update {
            it.copy(
                detalle = detalle,
                errorMessage = null
            )
        }
    }

    /**
     * Valida el comentario y lo añade al hilo local.
     * La llamada al repositorio/API se añadirá en la iteración de integración.
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

    fun limpiarError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}
