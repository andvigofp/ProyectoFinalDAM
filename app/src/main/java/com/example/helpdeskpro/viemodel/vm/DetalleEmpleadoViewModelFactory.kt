package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetalleEmpleadoViewModelFactory(
    private val application: Application,
    private val userId: String,
    private val incidenciaId: Long
) : ViewModelProvider.Factory {

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
