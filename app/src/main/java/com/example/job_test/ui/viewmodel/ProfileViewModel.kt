package com.example.job_test.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.job_test.data.model.AddEducationRequest
import com.example.job_test.data.model.AddExperienceRequest
import com.example.job_test.data.model.Education
import com.example.job_test.data.model.ErrorResponse
import com.example.job_test.data.model.Experience
import com.example.job_test.data.model.Profile
import com.example.job_test.data.model.ProfileEditRequest
import com.example.job_test.data.model.UserProfileResponse
import com.example.job_test.data.model.addResumeRequest
import com.example.job_test.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ProfileViewModel(private val repository: UserRepository):ViewModel() {
    private val _profile= MutableStateFlow<UserProfileResponse?>(null)   // change with ProfileResponse model
    val profile = _profile.asStateFlow()
    private val educationResponse = MutableStateFlow<ErrorResponse?>(null)
    val education = educationResponse.asStateFlow()
    private val experienceResponse = MutableStateFlow<ErrorResponse?>(null)
    val experience = experienceResponse.asStateFlow()
    private val resumeResponse = MutableStateFlow<ErrorResponse?>(null)
    val resume = resumeResponse.asStateFlow()


    init {
        viewModelScope.launch {
            getProfile()

        }
    }
    fun getProfile(onResult: (UserProfileResponse?) -> Unit={}){
        viewModelScope.launch {
            try {
                val profile = repository.getProfile()
                _profile.value = profile
                onResult(profile)
            } catch (e: Exception) {
                Log.d("ProfileViewModel", "getProfile: ${e.message}")
                onResult(null)
            }
        }
    }
    fun saveProfile(){

    }
    
    fun editProfile(
        bio: String?,
        age:String?,
        location:String?,
        phone_no:String?,
        gender: String?,
        firstName: String?,
        lastName: String?,
        email: String?,
        password: String?

    ) {
        viewModelScope.launch {
            try {
                repository.editProfile(
                    ProfileEditRequest(
                        bio = bio,
                        age = age,
                        location = location,
                        phone_no = phone_no,
                        gender = gender,
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        password = password
                    )
                )
                getProfile()
            } catch (e: Exception) {
                Log.d("ProfileViewModel", "editProfile: ${e.message}")
            }

        }
    }

    fun addExperience(experience: Experience, onComplete: () -> Unit={}) {
        viewModelScope.launch {
            try {
                repository.addExperience(AddExperienceRequest(listOf(experience)))
                getProfile()
                onComplete()
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error adding experience: ${e.message}")
                onComplete()
            }
        }
    }

    fun removeResume(){

    }

    fun addResume(resumePart: MultipartBody.Part, onComplete: () -> Unit={}) {
        viewModelScope.launch {
            try {
                val response = repository.addResume(addResumeRequest(resumePart))
                resumeResponse.value = response
                getProfile()
                onComplete()
            } catch (e: Exception) {
                Log.d("ProfileViewModel", "addResume: ${e.message}")
                onComplete()
            }
        }
    }
    fun createResumePart(file: File): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("resume", file.name, requestFile)
    }
    fun addEducation(education: Education, onComplete: () -> Unit={}) {
        val listOfEducation = listOf(education)
        viewModelScope.launch {
            try {
                val response = repository.addEducation(AddEducationRequest(listOfEducation))
                educationResponse.value = response
                getProfile()
                onComplete()
            } catch (e: Exception) {
                Log.d("ProfileViewModel", "addEducation: ${e.message}")
                onComplete()
            }
        }
    }

    fun addSkills(skills: List<String>, onComplete: () -> Unit={}) {
        viewModelScope.launch {
            try {
                val response = repository.addSkills(skills)
                getProfile()
                onComplete()
            } catch (e: Exception) {
                Log.d("ProfileViewModel", "addSkills: ${e.message}")
                onComplete()
            }
        }
    }

}
