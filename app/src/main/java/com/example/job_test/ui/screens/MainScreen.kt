package com.example.job_test.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.job_test.data.repository.UserRepository
import com.example.job_test.ui.viewmodel.HomeViewModel
import com.example.job_test.ui.viewmodel.JobsViewModel
import com.example.job_test.ui.viewmodel.ProfileViewModel
import com.example.job_test.ui.viewmodel.SavedJobsViewModel


data class BottomNavItem(
    var title:String,
    var selectedIcon:ImageVector,
    var unSelectedIcon:ImageVector,
    var hasNews:Boolean = false,
    var badgeCount: Int?=null
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen( context: Context, bottomNavItems:List<BottomNavItem>,onLogout:()->Unit) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val navController = rememberNavController()
    val jobsViewModel = remember { mutableStateOf<JobsViewModel?>(null) }
    val homeViewModel = remember { mutableStateOf<HomeViewModel?>(null) }
    val savedJobsViewModel = remember { mutableStateOf<SavedJobsViewModel?>(null) }
    val ProfileViewModel = remember { mutableStateOf<ProfileViewModel?>(null) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    // Sync the bottom navigation index with the current route
    LaunchedEffect(currentBackStackEntry) {
        val currentDestination = currentBackStackEntry?.destination?.route
        selectedIndex = bottomNavItems.indexOfFirst { it.title == currentDestination }
    }
    Scaffold(
        bottomBar = {
            NavigationBar() {
                bottomNavItems.forEachIndexed(){index,item->
                    NavigationBarItem(

                        label = { Text(text = item.title) },
                        selected = index==selectedIndex,
                        onClick = {
                            if(selectedIndex!=index) {
                                navController.navigate(item.title){
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                                selectedIndex = index
                            }
                        },
                        icon = {
                            BadgedBox(
                                badge = {
                                    if(item.badgeCount!=null){
                                        Badge {
                                            Text(text = item.badgeCount.toString())
                                        }
                                    }
                                    else if (item.hasNews){
                                        Badge()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (index == selectedIndex) item.selectedIcon else item.unSelectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        }
                    )

                }
            }
        }
    ) {
        NavHost(navController = navController, startDestination = bottomNavItems[0].title
        , modifier = Modifier.padding(it)
        ) {
            bottomNavItems.forEach { item->
                composable(item.title){
                    when(item.title){
                        "Home"->{
                            if(homeViewModel.value==null){
                                homeViewModel.value = HomeViewModel(UserRepository(context))
                            }
                            HomeScreen(context,homeViewModel.value!!)
                        }
                        "Profile"-> {

                            if(ProfileViewModel.value==null){
                                ProfileViewModel.value = ProfileViewModel(UserRepository(context))
                            }
                            ProfileScreen(context, ProfileViewModel.value!!, onLogout = {
                                onLogout()
                                navController.popBackStack()
                            })
                        }
                        "Settings"->{
                            SettingScreen()
                        }
                        "jobs"-> {
                            if(jobsViewModel.value==null){
                                jobsViewModel.value = JobsViewModel(UserRepository(context))
                            }
                            JobsScreen(jobsViewModel.value!!,context)
                        }
                        "Saved Jobs"-> {
                            if(savedJobsViewModel.value==null){
                                savedJobsViewModel.value = SavedJobsViewModel(UserRepository(context))
                            }
                            SavedJobsScreen(savedJobsViewModel.value!!,context)
                        }
                    }
                }
            }
        }
    }
}