package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CrearCuentaViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CrearCuentaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CrearCuentaViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
