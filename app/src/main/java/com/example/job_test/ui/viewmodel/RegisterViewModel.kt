package com.example.job_test.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.job_test.data.model.RegisterResponse
import com.example.job_test.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: AuthRepository):ViewModel() {
    private val _registerResult = MutableStateFlow<RegisterResponse?>(null)
    val registerResult  = _registerResult.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _isError = MutableStateFlow<Boolean?>(null)
    val isError = _isError.asStateFlow()

    fun registerUser(email: String, password: String, firstName: String, lastName: String) {
        _registerResult.value = null
        _isError.value = null
        _errorMessage.value = null
        viewModelScope.launch {
            try {
                val result = repository.registerUser(email, password, firstName, lastName);
                _isError.value = false
                _registerResult.value = result
            }
            catch (e: Exception) {
                _isError.value = true
                _errorMessage.value = e.message
            }
        }
    }
}