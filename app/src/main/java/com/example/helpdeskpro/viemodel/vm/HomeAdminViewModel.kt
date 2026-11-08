package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.helpdeskpro.data.repository.adminRepository.AdministracionRepository
import com.example.helpdeskpro.data.repository.adminRepository.inmemory.AdministracionRepositoryInMemory
import com.example.helpdeskpro.data.repository.inmemoryRepository.InMemoryHelpdeskStore
import com.example.helpdeskpro.R
import com.example.helpdeskpro.viemodel.ustate.HomeAdminUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeAdminViewModel(
    application: Application,
    val userId: String,
    private val administracionRepository: AdministracionRepository =
        AdministracionRepositoryInMemory(InMemoryHelpdeskStore())
) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(HomeAdminUiState())
    val uiState: StateFlow<HomeAdminUiState> = _uiState.asStateFlow()

    fun cargarResumen() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                _uiState.update {
                    it.copy(
                        resumen = administracionRepository.obtenerResumen(),
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
                            ?: getApplication<Application>().getString(R.string.error_load_admin_summary)
                    )
                }
            }
        }
    }
}

class HomeAdminViewModelFactory(
    private val application: Application,
    private val userId: String,
    private val administracionRepository: AdministracionRepository =
        AdministracionRepositoryInMemory(InMemoryHelpdeskStore())
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeAdminViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeAdminViewModel(application, userId, administracionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
