package com.example.job_test.data.model

data class Job(
    val description: String,
    val company_name: String,
    val company_location: String,
    val company_imageUrl: String?,
    val company_website: String,
    val company_phone_no: String,
    val applyUrl:String,
    val isBookmarked: Boolean,
    val id: String,
    val jobLocation: String,
    val jobType: String,
    val location: String,
    val postedAt: String,
    val salary: Int,
    val title: String,
    val updatedAt: String
)