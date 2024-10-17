package com.example.job_test.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.job_test.data.model.Profile
import com.example.job_test.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileViewModel(private val repository: UserRepository):ViewModel() {
    private val _profile= MutableStateFlow<Profile?>(null)   // change with ProfileResponse model
    val profile = _profile.asStateFlow()


    fun saveProfile(){

    }
    fun editProfile(){

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