package com.example.projectakhir.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectakhir.model.Penyakit
import com.example.projectakhir.repository.PenyakitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PenyakitViewModel : ViewModel() {

    private val repository = PenyakitRepository()

    private val _list = MutableStateFlow<List<Penyakit>>(emptyList())
    val list = _list.asStateFlow()

    var selectedPenyakit by androidx.compose.runtime.mutableStateOf<Penyakit?>(null)
        private set

    fun updateSelectedPenyakit(penyakit: Penyakit?) {
        selectedPenyakit = penyakit
    }

    fun refreshDetail() {
        val currentId = selectedPenyakit?.id_penyakit
        if (currentId != null) {
            viewModelScope.launch {
                _list.value = repository.getAll()
                selectedPenyakit = _list.value.find { it.id_penyakit == currentId }
            }
        }
    }
    
    // State for error messages
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun clearError() {
        _errorMessage.value = null
    }

    init {
        getAll()
    }

    fun getAll() {
        viewModelScope.launch {
            _list.value = repository.getAll()
        }
    }

    fun insert(
        nama: String,
        kategori: String,
        gejala: String,
        penularan: String,
        tingkat: String,
        deskripsi: String
    ) {
        viewModelScope.launch {
            repository.insert(
                nama,
                kategori,
                gejala,
                penularan,
                tingkat,
                deskripsi
            )
            getAll()
        }
    }

    fun update(
        id: String,
        nama: String,
        kategori: String,
        gejala: String,
        penularan: String,
        tingkat: String,
        deskripsi: String
    ) {
        viewModelScope.launch {
            repository.update(
                id,
                nama,
                kategori,
                gejala,
                penularan,
                tingkat,
                deskripsi
            )
            getAll()
        }
    }

    // ✅ FIX UTAMA ADA DI SINI
    fun delete(id: String) {
        viewModelScope.launch {
            try {
                repository.delete(id)
                getAll()
                selectedPenyakit = null
            } catch (e: Exception) {
                // Log error or show toaster
                println("Error deleting penyakit: ${e.message}")
                _errorMessage.value = "Gagal menghapus: Data sedang digunakan di Kasus atau Server Error"
            }
        }
    }
}
