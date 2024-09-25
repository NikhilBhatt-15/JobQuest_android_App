package com.example.job_test.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.job_test.data.model.JobsResponse
import com.example.job_test.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SavedJobsViewModel(private val repository: UserRepository): ViewModel() {
    private val _jobsResponse = MutableStateFlow<JobsResponse?>(null)
    val jobsResponse = _jobsResponse.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val result = repository.getSavedJobs()
                _jobsResponse.value = result
            } catch (e: Exception) {
                _jobsResponse.value = null
            }
        }
    }

    fun getSavedJobs(){
        viewModelScope.launch {
            try {
                val result = repository.getSavedJobs()
                _jobsResponse.value = result
            } catch (e: Exception) {
                _jobsResponse.value = null
            }
        }
    }


    fun unsaveJob(jobId: String) {
        viewModelScope.launch {
            try {
                repository.unsaveJob(jobId)
                _jobsResponse.value = repository.getSavedJobs()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}