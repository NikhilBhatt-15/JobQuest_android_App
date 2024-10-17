package com.example.job_test.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.job_test.utility.PreferenceManager

@Composable
fun ProfileScreen(context: Context, onLogout:()->Unit){
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "This is your Profile")
        var token = PreferenceManager.getToken(context)
        Text(text = "Token: $token")

        Button(onClick = {
            PreferenceManager.clearToken(context)
            onLogout()
        }) {
            Text(text = "Logout")
        }

    }
}