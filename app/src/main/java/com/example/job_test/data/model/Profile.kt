package com.example.job_test.data.model

data class Profile(
    val age: Int,
    val bio: String,
    val education: List<Education>,
    val email: String,
    val experience: List<Experience>,
    val firstname: String,
    val gender: String,
    val imageUrl: String,
    val lastname: String,
    val location: String,
    val phone_no: String,
    val resumeUrl: String,
    val skills: List<String>
)