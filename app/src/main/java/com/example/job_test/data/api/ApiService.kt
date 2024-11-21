package com.example.job_test.data.api


import com.example.job_test.data.model.AddEducationRequest
import com.example.job_test.data.model.AddExperienceRequest
import com.example.job_test.data.model.AddSkillsRequest
import com.example.job_test.data.model.CompaniesResponse
import com.example.job_test.data.model.Education
import com.example.job_test.data.model.ErrorResponse
import com.example.job_test.data.model.IsLoginResponse
import com.example.job_test.data.model.JobsResponse
import com.example.job_test.data.model.LoginRequest
import com.example.job_test.data.model.LoginResponse
import com.example.job_test.data.model.ProfileEditRequest
import com.example.job_test.data.model.RegisterRequest
import com.example.job_test.data.model.RegisterResponse
import com.example.job_test.data.model.UserProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @PUT("user/profile")
    suspend fun editProfile(
        @Body request: ProfileEditRequest
    ):Response<UserProfileResponse>

    @POST("user/profile/experience")
    suspend fun addExperience(
        @Body request: AddExperienceRequest
    ):Response<ErrorResponse>

    @Multipart
    @POST("user/profile")
    suspend fun createProfile(
        @Part("bio") bio: RequestBody,
        @Part avatar: MultipartBody.Part,
        @Part("location") location: RequestBody,
        @Part("age") age: RequestBody,
        @Part("gender") gender: RequestBody,
        @Part("phone_no") phoneNo: RequestBody
    ): Response<ErrorResponse>

    @POST("user/profile/education")
    suspend fun addEducation(
        @Body request: AddEducationRequest
    ):Response<ErrorResponse>

    @Multipart
    @PUT("user/profile/resume")
    suspend fun addResume(
        @Part resume: MultipartBody.Part,
    ):Response<ErrorResponse>

    @POST("user/profile/skills")
    suspend fun addSkills(
        @Body request: AddSkillsRequest
    ):Response<ErrorResponse>

    companion object{
        const val BASE_URL = "https://jobquest-backend-47ht.onrender.com/api/v1/"
    }

}