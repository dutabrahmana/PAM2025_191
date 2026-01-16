package com.example.projectakhir.model

data class Penyakit(
    val id_penyakit: String,
    val nama_penyakit: String,
    val kategori: String,
    val gejala: String,
    val cara_penularan: String,
    val tingkat_keparahan: String,
    val deskripsi: String,
    val created_at: String?,
    val updated_at: String?
)
