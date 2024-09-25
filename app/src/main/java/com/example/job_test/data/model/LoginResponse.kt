package com.example.job_test.data.model

data class LoginResponse(
    val message: String,
    val success: Boolean,
    val token: String,
    val user: User?
)