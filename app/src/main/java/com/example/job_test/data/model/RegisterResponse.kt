package com.example.job_test.data.model

data class RegisterResponse(
    val message: String,
    val success: Boolean,
    val user: User?
)