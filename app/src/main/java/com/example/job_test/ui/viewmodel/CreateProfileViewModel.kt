package com.example.job_test.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.job_test.data.model.CreateProfileRequest
import com.example.job_test.data.model.ErrorResponse
import com.example.job_test.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class CreateProfileViewModel(private val repository: UserRepository):ViewModel() {
    private val _profile = MutableStateFlow<ErrorResponse?>(null)
    val profile = _profile.asStateFlow()
    fun createProfile( bio: String,
                       avatarFile: File,
                       location: String,
                       age: Int,
                       gender: String,
                       phoneNo: String){
        Log.d("CreateProfileViewModel", "createProfile: $bio, $location, $age, $gender, $phoneNo")
        viewModelScope.launch {
            try {
                val avatarPart = createAvatarPart(avatarFile)
                val request = CreateProfileRequest(bio= bio, avatar= avatarPart, location= location, age= age,gender=gender, phone_no=phoneNo)
                val response = repository.createProfile(request)
                _profile.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun createAvatarPart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("avatar", file.name, requestFile)
    }
}