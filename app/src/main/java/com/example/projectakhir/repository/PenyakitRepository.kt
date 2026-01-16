package com.example.projectakhir.repository

import com.example.projectakhir.model.Penyakit
import com.example.projectakhir.network.RetrofitClient

class PenyakitRepository {

    private val api = RetrofitClient.apiService

    suspend fun getAll(): List<Penyakit> =
        api.getPenyakit().data ?: emptyList()

    suspend fun insert(
        nama: String,
        kategori: String,
        gejala: String,
        caraPenularan: String,
        tingkatKeparahan: String,
        deskripsi: String
    ) = api.insertPenyakit(
        nama,
        kategori,
        gejala,
        caraPenularan,
        tingkatKeparahan,
        deskripsi
    )

    suspend fun update(
        id: String,
        nama: String,
        kategori: String,
        gejala: String,
        caraPenularan: String,
        tingkatKeparahan: String,
        deskripsi: String
    ) = api.updatePenyakit(
        id,
        nama,
        kategori,
        gejala,
        caraPenularan,
        tingkatKeparahan,
        deskripsi
    )

    // ✅ FIX UTAMA ADA DI SINI
    suspend fun delete(id: String) =
        api.deletePenyakit(id)
}
