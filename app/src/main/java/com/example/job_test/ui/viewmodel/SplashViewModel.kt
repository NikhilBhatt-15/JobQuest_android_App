package com.example.job_test.ui.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.job_test.data.repository.AuthRepository
import com.example.job_test.utility.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(private val repository: AuthRepository):ViewModel() {
    private val _isLogin = MutableStateFlow<Boolean?>(null)
    val isLogin = _isLogin.asStateFlow()

    fun checkLogin(token:String?){
        Log.d("SplashViewModel", "checkLogin: ")
        _isLogin.value = null

        if(token.isNullOrEmpty()){
            _isLogin.value = false
            return
        }
        viewModelScope.launch {
            try {
                val result = repository.getUser()
                _isLogin.value = result.success ?: false
            }
            catch (e: Exception) {
                _isLogin.value = false
            }
        }
    }
}