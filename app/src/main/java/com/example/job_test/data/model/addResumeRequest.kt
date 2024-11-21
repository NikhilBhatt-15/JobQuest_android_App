package com.example.job_test.data.model

import okhttp3.MultipartBody

data class addResumeRequest(
    val resume: MultipartBody.Part,
)
