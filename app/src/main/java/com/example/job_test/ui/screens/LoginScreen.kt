package com.example.job_test.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.job_test.ui.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    context:Context,
    onLogin:()->Unit,
    onGoToRegister:()->Unit){
    var email by remember {
        mutableStateOf("")
    }
    var password by remember{
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    var loginError by remember {
        mutableStateOf<String?>(null)
    }

    val focusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val focusManager = LocalFocusManager.current

    var isError = viewModel.isError.collectAsState()
    LaunchedEffect(isError?.value) {
        isError.value?.let {
            if(isError.value==false){
                isLoading = false
                onLogin()
            }
            else{
                isLoading = false
                loginError = viewModel.errorMessage.value
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Login", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email, onValueChange = {
                email = it
            },
            label = { Text(text = "Email") },
            singleLine = true,
            modifier = Modifier.padding(8.dp).
                focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            )
        )
        OutlinedTextField(
            value = password, onValueChange = {
                password = it
            },
            label = { Text(text = "Password") },
            singleLine = true,
            modifier = Modifier.padding(8.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    Toast.makeText(context, "Logging in...", Toast.LENGTH_SHORT).show()
                    isLoading = true
                    loginError=null
                    viewModel.loginUser(context,email, password)
                }
            )
        )

        Button(enabled = !isLoading,onClick = {
            isLoading = true
            loginError=null
            viewModel.loginUser(context,email, password)
        }) {
            Text(text = if(isLoading) "Logging in..." else "Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Doesn't have an account?")
            Button(onClick = {
                onGoToRegister()
            }) {

                Text(text = "Register")
            }
        }
        loginError?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}