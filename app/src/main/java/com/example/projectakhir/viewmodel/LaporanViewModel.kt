package com.example.projectakhir.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projectakhir.model.Kasus
import com.example.projectakhir.model.Penyakit
import com.example.projectakhir.repository.KasusRepository
import com.example.projectakhir.repository.PenyakitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

// UI Model khusus untuk tampilan Laporan (Gabungan Kasus + Nama Penyakit)
data class LaporanUiModel(
    val idKasus: String,
    val namaPenyakit: String, // Ini yang direquest user
    val kelurahan: String,
    val kecamatan: String,
    val kota: String,
    val tanggal: String,
    val kasusBaru: Int,
    val sembuh: Int,
    val meninggal: Int,
    val dirawat: Int,
    val catatan: String
)

sealed class LaporanUiState {
    object Loading : LaporanUiState()
    data class Success(val data: List<LaporanUiModel>) : LaporanUiState()
    data class Error(val message: String) : LaporanUiState()
}

class LaporanViewModel(
    private val kasusRepository: KasusRepository = KasusRepository(),
    private val penyakitRepository: PenyakitRepository = PenyakitRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<LaporanUiState>(LaporanUiState.Loading)
    val uiState: StateFlow<LaporanUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = LaporanUiState.Loading
            try {
                // 1. Ambil semua data Kasus & Penyakit secara parallel (atau sequential basic)
                //    Idealnya parallel pakai async, tapi sequential juga oke untuk simplisitas.
                val listKasus = kasusRepository.getAll()
                val listPenyakit = penyakitRepository.getAll()

                // 2. Buat Map untuk lookup nama penyakit berdasarkan ID biar cepat
                //    Map<IdPenyakit, NamaPenyakit>
                val penyakitMap = listPenyakit.associateBy({ it.id_penyakit }, { it.nama_penyakit })

                // 3. Gabungkan (Mapping)
                val laporanList = listKasus.map { kasus ->
                    val namaPenyakit = penyakitMap[kasus.id_penyakit] ?: "Penyakit Tidak Dikenal (${kasus.id_penyakit})"
                    
                    LaporanUiModel(
                        idKasus = kasus.id_kasus,
                        namaPenyakit = namaPenyakit,
                        kelurahan = kasus.kelurahan,
                        kecamatan = kasus.kecamatan,
                        kota = kasus.kota,
                        tanggal = kasus.tanggal_laporan,
                        kasusBaru = kasus.jumlah_kasus_baru,
                        sembuh = kasus.jumlah_sembuh,
                        meninggal = kasus.jumlah_meninggal,
                        dirawat = kasus.jumlah_dalam_perawatan,
                        catatan = kasus.catatan
                    )
                }

                _uiState.value = LaporanUiState.Success(laporanList)

            } catch (e: IOException) {
                _uiState.value = LaporanUiState.Error("Terjadi kesalahan jaringan. Periksa koneksi internet anda.")
            } catch (e: Exception) {
                _uiState.value = LaporanUiState.Error("Terjadi kesalahan: ${e.localizedMessage}")
            }
        }
    }
}
