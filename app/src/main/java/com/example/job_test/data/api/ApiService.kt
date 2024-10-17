package com.example.job_test.data.api


import com.example.job_test.data.model.CompaniesResponse
import com.example.job_test.data.model.ErrorResponse
import com.example.job_test.data.model.IsLoginResponse
import com.example.job_test.data.model.JobsResponse
import com.example.job_test.data.model.LoginRequest
import com.example.job_test.data.model.LoginResponse
import com.example.job_test.data.model.RegisterRequest
import com.example.job_test.data.model.RegisterResponse
import com.example.job_test.data.model.UserProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("auth/profile")
    suspend fun user(): Response<IsLoginResponse>

    @GET("user/jobs")
    suspend fun jobs(): Response<JobsResponse>

    @GET("user/profile")
    suspend fun profile(): Response<UserProfileResponse>

    @GET("jobs/companies")
    suspend fun companies(): Response<CompaniesResponse>

    @PUT("jobs/save/{jobId}")
    suspend fun saveJob(
        @Path("jobId") jobId: String
    ):Response<ErrorResponse>

    @PUT("jobs/unsave/{jobId}")
    suspend fun unsaveJob(
        @Path("jobId") jobId: String
    ):Response<ErrorResponse>

    @GET("jobs/saved")
    suspend fun savedJobs():Response<JobsResponse>



    companion object{
        const val BASE_URL = "https://jobquest-backend-47ht.onrender.com/api/v1/"
    }

}