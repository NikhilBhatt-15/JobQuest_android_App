package com.example.job_test.data.network

import androidx.compose.ui.platform.LocalContext
import com.example.job_test.data.api.ApiService
import com.example.job_test.utility.PreferenceManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class AuthInterceptor(private val token:String):Interceptor{
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(request)
    }
}

object RetrofitInstance {

    fun getInstance(token: String): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

         val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AuthInterceptor(token))
            .build()

         val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(ApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        val api: ApiService by lazy {
            retrofit.create(ApiService::class.java)
        }
        return api
    }

}