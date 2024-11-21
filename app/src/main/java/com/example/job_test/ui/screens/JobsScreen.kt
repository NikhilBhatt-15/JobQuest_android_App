package com.example.job_test.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.job_test.R
import com.example.job_test.data.model.Job
import com.example.job_test.ui.viewmodel.JobsViewModel


@Composable
fun JobsScreen(viewModel: JobsViewModel = viewModel(), context: Context) {
    val jobsResponse by viewModel.jobsResponse.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(key1 = true) {
        viewModel.getJobs()
    }

    val filteredJobs = jobsResponse?.jobs?.filter { job ->
        job.title.contains(searchQuery, ignoreCase = true) ||
                job.jobType.contains(searchQuery, ignoreCase = true)
    } ?: emptyList()

    if (jobsResponse == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column {
            JobSearchComponent(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it }
            )
            LazyColumn {
                if (filteredJobs.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No jobs available", modifier = Modifier.padding(16.dp))
                        }
                    }
                } else {
                    items(filteredJobs) { job ->
                        JobCard(job = job, saveJob = viewModel::saveJob, unsaveJob = viewModel::unsaveJob, context)
                    }
                }
            }
        }
    }
}
@Composable
fun JobCard(job: Job, saveJob: ((String) -> Unit)?, unsaveJob: (String) -> Unit,context:Context) {

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),

        colors = CardDefaults.cardColors(containerColor= Color(0xFF4B4ACF))
    ) {

        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = job.company_imageUrl,
                        contentDescription = "Company Logo",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(color = Color.Transparent)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = job.title,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = job.company_name,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                    }
                }
                IconButton(onClick = {
                    if(job.isBookmarked) {
                        unsaveJob(job.id)
                    }
                    else {
                        if (saveJob != null) {
                            saveJob(job.id)
                        }
                    }
                }) {
                    Icon(
                        imageVector = if (job.isBookmarked) Icons.Default.Star else Icons.Default.AddCircle,
                        contentDescription = "Bookmark",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                JobTag(text = job.jobType)
                Spacer(modifier = Modifier.width(16.dp))
                JobTag(text = job.jobLocation)
                Spacer(modifier = Modifier.width(16.dp))
                JobTag(text = job.salary.toString())
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "13 min ago",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
                Text(
                    text = "30+ applicants",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
                Button(
                    onClick = {
                        if(job.applyUrl.isNullOrEmpty()){
                            return@Button
                        }
                        val applyUrl =  if (job.applyUrl.startsWith("http://") || job.applyUrl.startsWith("https://")) job.applyUrl else "http://${job.applyUrl}"
//                        on apply i want to open web browser with website url
                       try {
                           val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse( applyUrl))
                           browserIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                           startActivity(context, browserIntent, null)
                       }
                       catch (e:Exception){
                           e.printStackTrace()
                           Log.d("JobCard", "Error opening browser")
                       }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Apply", color = Color(0xFF4B4ACF))
                }
            }
        }
    }
}

@Composable
fun JobTag(text: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color.White.copy(alpha = 0.2f)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun JobSearchComponent(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    var searchText by remember { mutableStateOf(searchQuery) }
    val colors = MaterialTheme.colorScheme

    Box(modifier = Modifier.background(colors.background)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text(
                text = "Find Your",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E)
            )
            Text(
                text = "Dream Job",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A237E)
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    onSearchQueryChange(it)
                },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)),
                placeholder = { Text("Search for job title or type") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon")
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.LightGray.copy(alpha = 0.1f),
                    unfocusedContainerColor = Color.LightGray.copy(alpha = 0.1f),
                    cursorColor = Color(0xFF1A237E),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
        Image(
            painter = painterResource(id = R.drawable.paperplane),
            contentDescription = "Paper Plane",
            modifier = Modifier.align(Alignment.TopEnd).size(80.dp).background(colors.background).offset(x = (-20).dp, y = 20.dp)
        )
    }
}
