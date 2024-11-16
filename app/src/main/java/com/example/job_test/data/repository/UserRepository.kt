package com.example.job_test.data.repository

import android.content.Context
import com.example.job_test.data.model.CompaniesResponse
import com.example.job_test.data.model.ErrorResponse
import com.example.job_test.data.model.JobsResponse
import com.example.job_test.data.model.Profile
import com.example.job_test.data.model.ProfileEditRequest
import com.example.job_test.data.model.UserProfileResponse
import com.example.job_test.data.network.RetrofitInstance
import com.example.job_test.utility.PreferenceManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(context: Context) {
    private val token = PreferenceManager.getToken(context)?:""

    suspend fun getJobs(): JobsResponse {
        val response = RetrofitInstance.getInstance(token).jobs()
        return withContext(Dispatchers.IO){
            if(response.isSuccessful){
                return@withContext response.body()?: throw Exception("No data found")
            }
            else{
                var errorBody = response.errorBody()?.string()
                var gson = Gson()
                val errorResponse = errorBody?.let { gson.fromJson(it, ErrorResponse::class.java) }

                throw Exception(errorResponse?.message ?: "Can't Reach Server")
            }
        }
    }
    suspend fun getProfile():UserProfileResponse{
        val response = RetrofitInstance.getInstance(token).profile()
        return withContext(Dispatchers.IO){
            if(response.isSuccessful){
                return@withContext response.body()?: throw Exception("No data found")
            }
            else{
                var errorBody = response.errorBody()?.string()
                var gson = Gson()
                val errorResponse = errorBody?.let { gson.fromJson(it, ErrorResponse::class.java) }
                if(response.code() == 404){
                    throw Exception("Can't Reach Server")
                }
                if(response.code() == 401){
                    throw Exception("Unauthorized")
                }
                if(response.code() == 408){
                    throw Exception("Can't Reach Server")
                }

                throw Exception(errorResponse?.message ?: "Can't Reach Server")
            }
        }
    }
    suspend fun getCompanies():CompaniesResponse{
        val response = RetrofitInstance.getInstance(token).companies()
        return withContext(Dispatchers.IO){
            if(response.isSuccessful){
                return@withContext response.body()?: throw Exception("No data found")
            }
            else{
                var errorBody = response.errorBody()?.string()
                var gson = Gson()
                val errorResponse = errorBody?.let { gson.fromJson(it, ErrorResponse::class.java) }

                throw Exception(errorResponse?.message ?: "Can't Reach Server")
            }
        }
    }
    suspend fun saveJob(jobId: String){
        val response = RetrofitInstance.getInstance(token).saveJob(jobId)
        return withContext(Dispatchers.IO){
            if(response.isSuccessful){
                return@withContext
            }
            else{
                var errorBody = response.errorBody()?.string()
                var gson = Gson()
                val errorResponse = errorBody?.let { gson.fromJson(it, ErrorResponse::class.java) }

                throw Exception(errorResponse?.message ?: "Can't Reach Server")
            }
        }
    }

    suspend fun unsaveJob(jobId: String){
        val response = RetrofitInstance.getInstance(token).unsaveJob(jobId)
        return withContext(Dispatchers.IO){
            if(response.isSuccessful){
                return@withContext
            }
            else{
                var errorBody = response.errorBody()?.string()
                var gson = Gson()
                val errorResponse = errorBody?.let { gson.fromJson(it, ErrorResponse::class.java) }

                throw Exception(errorResponse?.message ?: "Can't Reach Server")
            }
        }
    }

    suspend fun getSavedJobs():JobsResponse{
        val response = RetrofitInstance.getInstance(token).savedJobs()
        return withContext(Dispatchers.IO){
            if(response.isSuccessful){
                return@withContext response.body()?: throw Exception("No data found")
            }
            else{
                var errorBody = response.errorBody()?.string()
                var gson = Gson()
                val errorResponse = errorBody?.let { gson.fromJson(it, ErrorResponse::class.java) }

                throw Exception(errorResponse?.message ?: "Can't Reach Server")
            }
        }
    }

    suspend fun editProfile(profileEditRequest: ProfileEditRequest):UserProfileResponse{

        val response = RetrofitInstance.getInstance(token).editProfile(profileEditRequest)
        return withContext(Dispatchers.IO){
            if(response.isSuccessful){
                return@withContext response.body()?: throw Exception("No data found")
            }
            else{
                var errorBody = response.errorBody()?.string()
                var gson = Gson()
                val errorResponse = errorBody?.let { gson.fromJson(it, ErrorResponse::class.java) }

                throw Exception(errorResponse?.message ?: "Can't Reach Server")
            }
        }
    }


}
