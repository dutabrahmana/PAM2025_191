package com.example.projectakhir.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectakhir.model.Kasus
import com.example.projectakhir.model.Penyakit
import com.example.projectakhir.repository.KasusRepository
import com.example.projectakhir.repository.PenyakitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KasusViewModel : ViewModel() {

    private val repository = KasusRepository()
    private val penyakitRepository = PenyakitRepository()

    private val _list = MutableStateFlow<List<Kasus>>(emptyList())
    val list = _list.asStateFlow()

    private val _listPenyakit = MutableStateFlow<List<Penyakit>>(emptyList())
    val listPenyakit = _listPenyakit.asStateFlow()

    // 🔥 sama pola dengan Penyakit
    var selectedKasus by androidx.compose.runtime.mutableStateOf<Kasus?>(null)
        private set
    
    fun updateSelectedKasus(kasus: Kasus?) {
        selectedKasus = kasus
    }

    fun refreshDetail() {
        val currentId = selectedKasus?.id_kasus
        if (currentId != null) {
            viewModelScope.launch {
                _list.value = repository.getAll()
                selectedKasus = _list.value.find { it.id_kasus == currentId }
            }
        }
    }

    init {
        getAll()
        getPenyakit()
    }

    fun getAll() {
        viewModelScope.launch {
            _list.value = repository.getAll()
        }
    }

    private fun getPenyakit() {
        viewModelScope.launch {
            _listPenyakit.value = penyakitRepository.getAll()
        }
    }

    fun insert(
        idPenyakit: String,
        kelurahan: String,
        kecamatan: String,
        kota: String,
        tanggal: String,
        baru: Int,
        dirawat: Int,
        sembuh: Int,
        meninggal: Int,
        catatan: String
    ) {
        viewModelScope.launch {
            repository.insert(
                idPenyakit,
                kelurahan,
                kecamatan,
                kota,
                tanggal,
                baru,
                dirawat,
                sembuh,
                meninggal,
                catatan
            )
            getAll()
        }
    }

    fun update(
        idKasus: String,
        idPenyakit: String,
        kelurahan: String,
        kecamatan: String,
        kota: String,
        tanggal: String,
        baru: Int,
        dirawat: Int,
        sembuh: Int,
        meninggal: Int,
        catatan: String
    ) {
        viewModelScope.launch {
            repository.update(
                idKasus,
                idPenyakit,
                kelurahan,
                kecamatan,
                kota,
                tanggal,
                baru,
                dirawat,
                sembuh,
                meninggal,
                catatan
            )
            getAll()
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            repository.delete(id)
            getAll()
        }
    }
}
