package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.helpdeskpro.data.repository.adminRepository.AdministracionRepository
import com.example.helpdeskpro.data.repository.incidenciasRepository.IncidenciasRepository
import com.example.helpdeskpro.data.repository.incidenciasRepository.inmemory.IncidenciasRepositoryInMemory
import com.example.helpdeskpro.data.repository.inmemoryRepository.InMemoryHelpdeskStore
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.ComentarioIncidenciaDTO
import com.example.helpdeskpro.modelo.DetalleIncidenciaTecnicoDTO
import com.example.helpdeskpro.viemodel.ustate.DetalleIncidenciaTecnicoAdminUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Mantiene los datos y las acciones de la pantalla de detalle de incidencia del técnico y del administrador.
 *
 * @property uiState flujo observable del estado de la pantalla.
 * @param application aplicación Android usada para obtener recursos.
 * @param userId identificador del usuario autenticado.
 * @param incidenciaId identificador de la incidencia mostrada.
 * @param esAdmin indica si la pantalla la usa un administrador (puede asignar técnico).
 * @param administracionRepository repositorio para obtener los técnicos disponibles.
 */
class DetalleIncidenciaTecnicoAdminViewModel(
    application: Application,
    val userId: String,
    val incidenciaId: Long,
    private val incidenciasRepository: IncidenciasRepository =
        IncidenciasRepositoryInMemory(InMemoryHelpdeskStore()),
    val esAdmin: Boolean = false,
    private val administracionRepository: AdministracionRepository? = null
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(DetalleIncidenciaTecnicoAdminUiState())
    val uiState: StateFlow<DetalleIncidenciaTecnicoAdminUiState> = _uiState.asStateFlow()

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
     * Actualiza el técnico seleccionado en la pantalla.
     *
     * @param tecnico nombre del técnico seleccionado.
     */
    fun onTecnicoChange(tecnico: String) {
        _uiState.update {
            it.copy(tecnicoSeleccionado = tecnico)
        }
    }

    /**
     * Carga los técnicos activos que se pueden asignar. Solo para el administrador.
     */
    fun cargarTecnicos() {
        val repositorio = administracionRepository ?: return
        if (!esAdmin) return
        viewModelScope.launch {
            try {
                val tecnicos = repositorio.obtenerUsuarios()
                    .filter { it.rol == "TECNICO" && it.activo }
                _uiState.update { it.copy(tecnicos = tecnicos) }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { it.copy(tecnicos = emptyList()) }
            }
        }
    }

    /**
     * Asigna el técnico seleccionado a la incidencia y recarga el detalle.
     *
     * @param onTecnicoAsignado acción que recibe el nombre del técnico asignado.
     */
    fun asignarTecnico(onTecnicoAsignado: (String) -> Unit = {}) {
        val nombre = _uiState.value.tecnicoSeleccionado
        val tecnico = _uiState.value.tecnicos.firstOrNull {
            "${it.nombre} ${it.apellidos}".trim() == nombre
        } ?: return
        viewModelScope.launch {
            try {
                incidenciasRepository.asignarTecnico(incidenciaId, tecnico.id)
                val detalle = incidenciasRepository.obtenerDetalle(incidenciaId)
                _uiState.update {
                    it.copy(detalle = detalle, tecnicoSeleccionado = "", errorMessage = null)
                }
                onTecnicoAsignado(nombre)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = exception.message
                            ?: getApplication<Application>().getString(R.string.error_assign_technician)
                    )
                }
            }
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
        if (estado.isBlank()) return
        viewModelScope.launch {
            try {
                incidenciasRepository.actualizarEstado(incidenciaId, estado)
                val detalle = incidenciasRepository.obtenerDetalle(incidenciaId)
                _uiState.update { it.copy(detalle = detalle, errorMessage = null) }
                onEstadoValido(estado)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = exception.message
                            ?: getApplication<Application>().getString(R.string.error_update_incident)
                    )
                }
            }
        }
    }

    /**
     * Entrega la solución recortada si contiene texto.
     *
     * @param onSolucionValida acción que recibe la solución válida.
     */
    fun registrarSolucion(onSolucionValida: (String) -> Unit = {}) {
        val solucion = _uiState.value.solucion.trim()
        if (solucion.isEmpty()) return
        viewModelScope.launch {
            try {
                incidenciasRepository.registrarSolucion(incidenciaId, solucion)
                val detalle = incidenciasRepository.obtenerDetalle(incidenciaId)
                _uiState.update { it.copy(detalle = detalle, errorMessage = null) }
                onSolucionValida(solucion)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = exception.message
                            ?: getApplication<Application>().getString(R.string.error_register_solution)
                    )
                }
            }
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
     * Carga el detalle de la incidencia desde el repositorio.
     */
    fun cargarDetalle() {

        if (_uiState.value.isLoading) return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val detalle = incidenciasRepository.obtenerDetalle(incidenciaId)

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
     * Valida el comentario y lo guarda mediante el repositorio.
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

        viewModelScope.launch {
            try {
                incidenciasRepository.agregarComentario(incidenciaId, comentario)
                val detalle = incidenciasRepository.obtenerDetalle(incidenciaId)
                _uiState.update {
                    it.copy(
                        comentario = "",
                        comentarioError = null,
                        detalle = detalle,
                        errorMessage = null
                    )
                }
                onComentarioValido(comentario)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = exception.message
                            ?: getApplication<Application>().getString(R.string.error_send_comment)
                    )
                }
            }
        }
    }

    /** Elimina el mensaje de error actual. */
    fun limpiarError() {
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }
}

/**
 * Crea instancias de [DetalleIncidenciaTecnicoAdminViewModel] con los identificadores de usuario e incidencia.
 *
 * @param application aplicación Android asociada al ViewModel.
 * @param userId identificador del usuario autenticado.
 * @param incidenciaId identificador de la incidencia seleccionada.
 * @param esAdmin indica si la pantalla la usa un administrador.
 * @param administracionRepository repositorio para obtener los técnicos disponibles.
 */
class DetalleIncidenciaTecnicoAdminViewModelFactory(
    private val application: Application,
    private val userId: String,
    private val incidenciaId: Long,
    private val incidenciasRepository: IncidenciasRepository =
        IncidenciasRepositoryInMemory(InMemoryHelpdeskStore()),
    private val esAdmin: Boolean = false,
    private val administracionRepository: AdministracionRepository? = null
) : ViewModelProvider.Factory {

    /**
     * Crea el ViewModel solicitado o falla si el tipo no está soportado.
     *
     * @param modelClass clase del ViewModel que se debe crear.
     * @return instancia del tipo solicitado.
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetalleIncidenciaTecnicoAdminViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetalleIncidenciaTecnicoAdminViewModel(
                application = application,
                userId = userId,
                incidenciaId = incidenciaId,
                incidenciasRepository = incidenciasRepository,
                esAdmin = esAdmin,
                administracionRepository = administracionRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
