package com.example.job_test.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.collection.emptyLongSet
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.job_test.data.model.LoginResponse
import com.example.job_test.data.repository.AuthRepository
import com.example.job_test.utility.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
):ViewModel() {
    private val _loginResponse = MutableStateFlow<LoginResponse?>(null)
    val loginResponse = _loginResponse.asStateFlow()

    private val _isError = MutableStateFlow<Boolean?>(null)
    val isError = _isError.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    @SuppressLint("SuspiciousIndentation")
    fun loginUser(context: Context, email: String, password: String) {
       viewModelScope.launch {
           _loginResponse.value = null
              _isError.value = null
              try {
                val result = repository.loginUser(email, password)
                    _isError.value = false
                  PreferenceManager.saveToken(context, result.token)
                _loginResponse.value = result

              } catch (e: Exception) {

                    _isError.value = true
                    _errorMessage.value = e.message ?: "Login failed"
                    _loginResponse.value = null
              }
       }
    }




}