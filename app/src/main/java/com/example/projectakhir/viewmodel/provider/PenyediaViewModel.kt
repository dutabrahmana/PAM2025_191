package com.example.projectakhir.viewmodel.provider

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.projectakhir.repository.AuthRepository
import com.example.projectakhir.viewmodel.AuthViewModel

object PenyediaViewModel {

    val Factory = viewModelFactory {
        initializer {
            AuthViewModel(
                AuthRepository()
            )
        }
    }
}
