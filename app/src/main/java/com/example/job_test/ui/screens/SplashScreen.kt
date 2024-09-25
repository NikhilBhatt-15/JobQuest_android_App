package com.example.job_test.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.job_test.ui.viewmodel.SplashViewModel
import com.example.job_test.utility.PreferenceManager

@Composable
fun SplashScreen(
    viewModel: SplashViewModel= viewModel(),
    context: Context,
    onLogin:()->Unit,
    onProfile:()->Unit
) {

    Log.d("SplashScreen", "SplashScreen: ")

    val token = PreferenceManager.getToken(context)


    LaunchedEffect(Unit) {
        Log.d("SplashScreen", "SplashScreen: $token")
        viewModel.checkLogin(token)
    }
    val isLogin = viewModel.isLogin.collectAsState()

    Log.d("SplashScreen", "SplashScreen: ${isLogin.value}")
    if(isLogin.value ==null){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Text(text = "Loading...")
        }
    }
    else{
        if(isLogin.value ==true){
            onProfile()
        }
        else{
            onLogin()
        }
    }
}
