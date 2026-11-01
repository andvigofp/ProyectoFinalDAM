package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NuevaIncidenciaViewModelFactory(
    private val application: Application,
    private val userId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NuevaIncidenciaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NuevaIncidenciaViewModel(
                application = application,
                userId = userId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
