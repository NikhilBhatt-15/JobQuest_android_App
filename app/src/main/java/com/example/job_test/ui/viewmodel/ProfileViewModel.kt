package com.example.job_test.ui.viewmodel

import android.util.Log
import androidx.core.app.GrammaticalInflectionManagerCompat.GrammaticalGender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.job_test.data.model.Profile
import com.example.job_test.data.model.ProfileEditRequest
import com.example.job_test.data.model.UserProfileResponse
import com.example.job_test.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository):ViewModel() {
    private val _profile= MutableStateFlow<UserProfileResponse?>(null)   // change with ProfileResponse model
    val profile = _profile.asStateFlow()


    init {
        viewModelScope.launch {
            getProfile()

        }
    }

    private fun getProfile(){
        viewModelScope.launch {
            try{
                val profile = repository.getProfile()
                _profile.value = profile
            }
            catch (e: Exception){
                Log.d("ProfileViewModel", "getProfile: ${e.message}")
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

    fun addResume(){

    }

    fun removeResume(){

    }

    fun addExperience(){

    }

    fun addEducation(){

    }
}
