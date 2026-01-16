package com.example.projectakhir.network

data class ApiResponse<T>(
    val status: Boolean,
    val message: String,
    val data: T? = null
)
