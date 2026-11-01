package com.example.helpdeskpro.viemodel.vm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HomeEmpleadoViewModelFactory(
    private val application: Application,
    private val userId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeEmpleadoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeEmpleadoViewModel(
                application = application,
                userId = userId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
