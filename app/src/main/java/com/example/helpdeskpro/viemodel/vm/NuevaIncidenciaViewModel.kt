package com.example.helpdeskpro.viemodel.vm


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.helpdeskpro.data.repository.incidenciasRepository.IncidenciasRepository
import com.example.helpdeskpro.data.repository.incidenciasRepository.inmemory.IncidenciasRepositoryInMemory
import com.example.helpdeskpro.data.repository.inmemoryRepository.InMemoryHelpdeskStore
import com.example.helpdeskpro.R
import com.example.helpdeskpro.modelo.NuevaIncidenciaDTO
import com.example.helpdeskpro.viemodel.ustate.NuevaIncidenciaUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch

/**
 * Mantiene y valida el formulario de creación de incidencias.
 *
 * @property uiState flujo observable del estado del formulario.
 * @param application aplicación Android usada para acceder a recursos.
 * @param userId identificador del usuario que registra la incidencia.
 */
class NuevaIncidenciaViewModel(
    application: Application,
    val userId: String,
    private val incidenciasRepository: IncidenciasRepository =
        IncidenciasRepositoryInMemory(InMemoryHelpdeskStore())
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(NuevaIncidenciaUiState())
    val uiState: StateFlow<NuevaIncidenciaUiState> = _uiState.asStateFlow()

    /**
     * Actualiza el título y limpia su error.
     *
     * @param titulo nuevo título introducido.
     */
    fun onTituloChange(titulo: String) {
        _uiState.update {
            it.copy(
                titulo = titulo,
                tituloError = null,
                errorMessage = null,
                incidenciaCreada = false
            )
        }
    }

    /**
     * Actualiza la descripción y limpia su error.
     *
     * @param descripcion nueva descripción introducida.
     */
    fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(
                descripcion = descripcion,
                descripcionError = null,
                errorMessage = null,
                incidenciaCreada = false
            )
        }
    }

    /**
     * Actualiza la categoría y limpia su error.
     *
     * @param categoria categoría seleccionada.
     */
    fun onCategoriaChange(categoria: String) {
        _uiState.update {
            it.copy(
                categoria = categoria,
                categoriaError = null,
                errorMessage = null,
                incidenciaCreada = false
            )
        }
    }

    /**
     * Actualiza la prioridad y limpia su error.
     *
     * @param prioridad prioridad seleccionada.
     */
    fun onPrioridadChange(prioridad: String) {
        _uiState.update {
            it.copy(
                prioridad = prioridad,
                prioridadError = null,
                errorMessage = null,
                incidenciaCreada = false
            )
        }
    }

    /**
     * Valida el formulario y registra la incidencia mediante el repositorio.
     */
    fun crearIncidencia() {

        val currentState = _uiState.value

        val titulo = currentState.titulo.trim()
        val descripcion = currentState.descripcion.trim()
        val categoria = currentState.categoria.trim()
        val prioridad = currentState.prioridad.trim()

        val tituloError = if (titulo.isEmpty()) {
            getApplication<Application>().getString(R.string.error_title_required)
        } else {
            null
        }

        val descripcionError = if (descripcion.isEmpty()) {
            getApplication<Application>().getString(R.string.error_description_required)
        } else {
            null
        }

        val categoriaError = if (categoria.isEmpty()) {
            getApplication<Application>().getString(R.string.error_category_required)
        } else {
            null
        }

        val prioridadError = if (prioridad.isEmpty()) {
            getApplication<Application>().getString(R.string.error_priority_required)
        } else {
            null
        }

        _uiState.update {
            it.copy(
                titulo = titulo,
                descripcion = descripcion,
                categoria = categoria,
                prioridad = prioridad,
                tituloError = tituloError,
                descripcionError = descripcionError,
                categoriaError = categoriaError,
                prioridadError = prioridadError,
                errorMessage = null,
                incidenciaCreada = false
            )
        }

        val formularioValido = listOf(
            tituloError,
            descripcionError,
            categoriaError,
            prioridadError
        ).all { it == null }

        if (formularioValido) {
            val datos = NuevaIncidenciaDTO(
                    titulo = titulo,
                    descripcion = descripcion,
                    categoria = categoria,
                    prioridad = prioridad
                )
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                try {
                    incidenciasRepository.crearIncidencia(userId, datos)
                    _uiState.update {
                        it.copy(isLoading = false, incidenciaCreada = true, errorMessage = null)
                    }
                } catch (exception: CancellationException) {
                    throw exception
                } catch (exception: Exception) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message
                                ?: getApplication<Application>().getString(R.string.error_create_incident)
                        )
                    }
                }
            }
        }
    }

    /** Restablece el formulario a su estado inicial. */
    fun limpiarFormulario() {
        _uiState.update {
            NuevaIncidenciaUiState()
        }
    }
}

/**
 * Crea instancias de [NuevaIncidenciaViewModel] asociadas a un usuario.
 *
 * @param application aplicación Android asociada al ViewModel.
 * @param userId identificador del usuario que registra la incidencia.
 */
class NuevaIncidenciaViewModelFactory(
    private val application: Application,
    private val userId: String,
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
        if (modelClass.isAssignableFrom(NuevaIncidenciaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NuevaIncidenciaViewModel(
                application = application,
                userId = userId,
                incidenciasRepository = incidenciasRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}