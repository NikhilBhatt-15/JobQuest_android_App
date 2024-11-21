package com.example.job_test.data.model

import okhttp3.MultipartBody
import retrofit2.http.Multipart

data class CreateProfileRequest(
    val bio: String,
    val avatar: MultipartBody.Part,
    val location: String,
    val age: Int,
    val gender: String,
    val phone_no: String
)
