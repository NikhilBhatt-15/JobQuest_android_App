package com.example.job_test.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.job_test.ui.viewmodel.SavedJobsViewModel

@Composable
fun SavedJobsScreen(viewModel: SavedJobsViewModel,context:Context) {
    val jobsResponse by viewModel.jobsResponse.collectAsState()
    LaunchedEffect(key1 = true) {
        viewModel.getSavedJobs()
    }
    LazyColumn {
        if (jobsResponse?.jobs.isNullOrEmpty()) {
            item {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No jobs available", modifier = Modifier.padding(16.dp))
                }
            }
        } else {
            jobsResponse?.jobs?.let { jobs ->
                items(jobs) { job ->
                    JobCard(job = job, saveJob =null, unsaveJob = viewModel::unsaveJob, context)
                }
            }
        }
    }
}