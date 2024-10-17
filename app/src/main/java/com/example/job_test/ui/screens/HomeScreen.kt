package com.example.job_test.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.job_test.data.model.Company
import com.example.job_test.data.model.Job
import com.example.job_test.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    context: Context,
    viewModel: HomeViewModel
) {
    val profile by viewModel.profile.collectAsState()
    val companiesResponse by viewModel.companies.collectAsState()
    val jobsResponse by viewModel.jobsResponse.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.getJobs()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(14.dp)
    ) {
        JobSearchCard(
            userName = profile?.profile?.firstname ?: "John Doe",
            userEmail = profile?.profile?.email ?: "",
            userAvatar = profile?.profile?.imageUrl ?: ""
        )
        if (companiesResponse!=null) {
            Text(
                text = "Top Companies",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            companiesResponse?.let { TopCompanies(it.companies) }
        }
        if(jobsResponse!=null){
            Text(
                text = "Popular Jobs",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            jobsResponse?.let { PopularJobs(it.jobs, onSave = viewModel::saveJob, onUnSave = viewModel::unsaveJob,context) }
        }
    }
}


@Preview
@Composable
fun UserProfile(
    userName: String = "John Doe",
    userEmail: String = "",
    userAvatar: String = ""
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AsyncImage(
            model = userAvatar,
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = userName.uppercase(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = userEmail,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun JobSearchField(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Column {
        Text(
            text = "Find a job",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color  = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = {
                Text(
                    "Job title or keyword",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),

            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        )
    }
}

@Composable
fun JobSearchCard(
    userName: String,
    userEmail: String,
    userAvatar: String
) {
    var searchQuery by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
//            modifier = Modifier.padding(16.dp)
        ) {
            UserProfile(
                userName = userName,
                userEmail = userEmail,
                userAvatar = userAvatar
            )
            Spacer(modifier = Modifier.height(24.dp))
            JobSearchField(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )
        }
    }
}

@Composable
fun TopCompanies(companies: List<Company>) {
    LazyRow(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        items(companies) { company ->
            CompanyItem(company = company)
        }
    }
}

@Composable
fun CompanyItem(company:Company) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 8.dp) // Horizontal padding for each item
    ) {
        Card(
            modifier = Modifier
                .size(30.dp) // Size of the circular image
                .clip(CircleShape), // Make it circular
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Softer color
        ) {
            AsyncImage(
                model = company.imageUrl,
                contentDescription = company.company,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape) // Ensure the image is clipped to circle
            )
        }
        Spacer(modifier = Modifier.height(8.dp)) // Spacer between image and name
        Text(
            text = company.company,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurface, // Text color for the company name
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun PopularJobs(jobs:List<Job>,onSave:(String)->Unit,onUnSave:(String)->Unit,context: Context) {
    LazyColumn {
        items(jobs){ job->
            JobCard(job = job, saveJob = onSave, unsaveJob = onUnSave,context)
        }
    }
}