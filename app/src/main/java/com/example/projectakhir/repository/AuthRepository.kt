package com.example.projectakhir.repository

import com.example.projectakhir.network.RetrofitClient

class AuthRepository {

    private val api = RetrofitClient.authApiService

    suspend fun register(
        username: String,
        email: String,
        password: String,
        noHp: String
    ) = api.register(username, email, password, noHp)

    suspend fun login(
        username: String,
        password: String
    ) = api.login(username, password)
}
