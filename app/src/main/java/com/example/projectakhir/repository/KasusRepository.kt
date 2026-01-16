package com.example.projectakhir.repository

import com.example.projectakhir.model.Kasus
import com.example.projectakhir.network.RetrofitClient

class KasusRepository {

    private val api = RetrofitClient.apiService

    // ================= GET ALL =================
    suspend fun getAll(): List<Kasus> =
        api.getKasus().data ?: emptyList()

    // ================= INSERT =================
    suspend fun insert(
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
    ) = api.insertKasus(
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

    // ================= UPDATE =================
    suspend fun update(
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
    ) = api.updateKasus(
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

    // ================= DELETE =================
    suspend fun delete(id: String) =
        api.deleteKasus(id)
}
