package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecuperarContrasenaViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecuperarContrasenaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecuperarContrasenaViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
