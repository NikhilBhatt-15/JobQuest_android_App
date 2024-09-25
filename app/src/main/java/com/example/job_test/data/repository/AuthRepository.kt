package com.example.job_test.data.repository


import android.content.Context
import com.example.job_test.data.model.ErrorResponse
import com.example.job_test.data.model.IsLoginResponse
import com.example.job_test.data.model.LoginResponse
import com.example.job_test.data.model.RegisterRequest
import com.example.job_test.data.model.RegisterResponse
import com.example.job_test.data.network.RetrofitInstance
import com.example.job_test.utility.PreferenceManager
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(context: Context) {
    private val token = PreferenceManager.getToken(context)?:""
    suspend fun registerUser(email: String, password: String, firstName: String, lastName: String):RegisterResponse {
        val response = RetrofitInstance.getInstance(token).register(RegisterRequest(email, password, firstName, lastName))
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

    suspend fun loginUser(email: String, password: String):LoginResponse {
        val response = RetrofitInstance.getInstance(token).login(com.example.job_test.data.model.LoginRequest(email, password))
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

    suspend fun getUser():IsLoginResponse {
        val response =  RetrofitInstance.getInstance(token).user()
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