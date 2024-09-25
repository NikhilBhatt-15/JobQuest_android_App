package com.example.job_test.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.job_test.data.model.CompaniesResponse
import com.example.job_test.data.model.JobsResponse
import com.example.job_test.data.model.UserProfileResponse
import com.example.job_test.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository):ViewModel() {
    private val _profile = MutableStateFlow<UserProfileResponse?>(null)
    val profile = _profile.asStateFlow()

    private val _companies = MutableStateFlow<CompaniesResponse?>(null)
    val companies = _companies.asStateFlow()

    private val _jobsResponse = MutableStateFlow<JobsResponse?>(null)
    val jobsResponse = _jobsResponse.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val result = repository.getProfile()
                _profile.value = result
            } catch (e: Exception) {
                _profile.value = null
            }
            try {
                val result = repository.getCompanies()
                _companies.value = result
            } catch (e: Exception) {
                _companies.value = null
            }
            try {
                val result = repository.getJobs()
                _jobsResponse.value = result
            } catch (e: Exception) {
                _jobsResponse.value = null
            }
        }

    }
    fun getJobs(){
        viewModelScope.launch {
            try {
                val result = repository.getJobs()
                _jobsResponse.value = result
            } catch (e: Exception) {
                _jobsResponse.value = null
            }
        }
    }
    fun saveJob(jobId: String){
        viewModelScope.launch {
            try {
                repository.saveJob(jobId)
                _jobsResponse.value = repository.getJobs()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun unsaveJob(jobId: String){
        viewModelScope.launch {
            try {
                repository.unsaveJob(jobId)
                _jobsResponse.value = repository.getJobs()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}