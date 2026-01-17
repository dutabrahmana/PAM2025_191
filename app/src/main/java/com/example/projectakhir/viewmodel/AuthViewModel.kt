package com.example.projectakhir.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectakhir.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState = _authState.asStateFlow()

    fun register(username: String, email: String, password: String, noHp: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val res = authRepository.register(username, email, password, noHp)
                _authState.value =
                    if (res.status) AuthState.Success(res.message)
                    else AuthState.Error(res.message)
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Server error")
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val res = authRepository.login(username, password)
                _authState.value =
                    if (res.status) AuthState.Success(res.message)
                    else AuthState.Error(res.message)
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Server error")
            }
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}
