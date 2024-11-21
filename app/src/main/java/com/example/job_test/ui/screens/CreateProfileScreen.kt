package com.example.job_test.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.job_test.ui.viewmodel.CreateProfileViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Composable
fun CreateProfileScreen(viewModel: CreateProfileViewModel = viewModel(), context: Context, onProfileCreated: () -> Unit) {
    val response = viewModel.profile.collectAsState()
    var bio by remember { mutableStateOf("") }
    var avatarUri by remember { mutableStateOf<Uri?>(null) }
    var avatarBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var location by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("MALE") }
    var phoneNo by remember { mutableStateOf("") }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        avatarUri = uri
        avatarUri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val bitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
            avatarBitmap = bitmap?.asImageBitmap()
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Create Profile", style = MaterialTheme.typography.headlineLarge)
        Button(onClick = { launcher.launch("image/*") }) {
            Text("Select Avatar")
        }
        avatarBitmap?.let { bitmap ->
            Image(bitmap = bitmap, contentDescription = "Selected Avatar", modifier = Modifier.size(128.dp))
        }
        OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("Bio") })
        OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") })
        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") })

        Text("Gender")
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = gender == "MALE",
                onClick = { gender = "MALE" }
            )
            Text("Male")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(
                selected = gender == "FEMALE",
                onClick = { gender = "FEMALE" }
            )
            Text("Female")
        }

        OutlinedTextField(value = phoneNo, singleLine = true, onValueChange = { phoneNo = it }, label = { Text("Phone Number") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            avatarUri?.let { uri ->
                val avatarFile = createFileFromUri(context, uri)
                val avatarPart = avatarFile?.let { viewModel.createAvatarPart(it) }
                if (avatarPart != null) {
                    viewModel.createProfile(
                        bio = bio,
                        avatarFile = avatarFile,
                        location = location,
                        age = age.toIntOrNull() ?: 0,
                        gender = gender,
                        phoneNo = phoneNo
                    )
                    if (response.value?.success == true) {
                        onProfileCreated()
                    }
                }
            }
        }) {
            Text("Create Profile")
        }
    }
}
fun createFileFromUri(context: Context, uri: Uri): File? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("avatar", ".jpg", context.cacheDir)
        val outputStream = FileOutputStream(tempFile)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}