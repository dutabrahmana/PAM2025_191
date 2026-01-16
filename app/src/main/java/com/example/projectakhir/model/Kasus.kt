package com.example.projectakhir.model



data class Kasus(
    val id_kasus: String,
    val id_penyakit: String,
    val kelurahan: String,
    val kecamatan: String,
    val kota: String,
    val tanggal_laporan: String,
    val jumlah_kasus_baru: Int,
    val jumlah_dalam_perawatan: Int,
    val jumlah_sembuh: Int,
    val jumlah_meninggal: Int,
    val catatan: String
)
