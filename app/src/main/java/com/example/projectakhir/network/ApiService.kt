package com.example.projectakhir.network

import androidx.navigation.NavBackStackEntry
import com.example.projectakhir.model.Kasus
import com.example.projectakhir.model.Penyakit
import retrofit2.http.*

interface ApiService {

    @GET("penyakit/get.php")
    suspend fun getPenyakit(): ApiResponse<List<Penyakit>>

    @FormUrlEncoded
    @POST("penyakit/insert.php")
    suspend fun insertPenyakit(
        @Field("nama_penyakit") nama: String,
        @Field("kategori") kategori: String,
        @Field("gejala") gejala: String,
        @Field("cara_penularan") caraPenularan: String,
        @Field("tingkat_keparahan") tingkatKeparahan: String,
        @Field("deskripsi") deskripsi: String
    ): ApiResponse<Unit>

    @FormUrlEncoded
    @POST("penyakit/update.php")
    suspend fun updatePenyakit(
        @Field("id_penyakit") id: String,
        @Field("nama_penyakit") nama: String,
        @Field("kategori") kategori: String,
        @Field("gejala") gejala: String,
        @Field("cara_penularan") caraPenularan: String,
        @Field("tingkat_keparahan") tingkatKeparahan: String,
        @Field("deskripsi") deskripsi: String
    ): ApiResponse<Unit>

    @FormUrlEncoded
    @POST("penyakit/delete.php")
    suspend fun deletePenyakit(
        @Field("id_penyakit") id: String
    ): ApiResponse<Unit>



    @GET("kasus/get.php")
    suspend fun getKasus(): ApiResponse<List<Kasus>>

    @FormUrlEncoded
    @POST("kasus/insert.php")
    suspend fun insertKasus(
        @Field("id_penyakit") idPenyakit: String,
        @Field("kelurahan") kelurahan: String,
        @Field("kecamatan") kecamatan: String,
        @Field("kota") kota: String,
        @Field("tanggal_laporan") tanggal: String,
        @Field("jumlah_kasus_baru") baru: Int,
        @Field("jumlah_dalam_perawatan") dirawat: Int,
        @Field("jumlah_sembuh") sembuh: Int,
        @Field("jumlah_meninggal") meninggal: Int,
        @Field("catatan") catatan: String
    ): ApiResponse<Unit>

    @FormUrlEncoded
    @POST("kasus/update.php")
    suspend fun updateKasus(
        @Field("id_kasus") idKasus: String,
        @Field("id_penyakit") idPenyakit: String,
        @Field("kelurahan") kelurahan: String,
        @Field("kecamatan") kecamatan: String,
        @Field("kota") kota: String,
        @Field("tanggal_laporan") tanggal: String,
        @Field("jumlah_kasus_baru") baru: Int,
        @Field("jumlah_dalam_perawatan") dirawat: Int,
        @Field("jumlah_sembuh") sembuh: Int,
        @Field("jumlah_meninggal") meninggal: Int,
        @Field("catatan") catatan: String
    ): ApiResponse<Unit>

    @FormUrlEncoded
    @POST("kasus/delete.php")
    suspend fun deleteKasus(
        @Field("id_kasus") idKasus: String
    ): ApiResponse<Unit>

}


