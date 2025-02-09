package com.example.healthfusion.healthFusionNav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination?.route

    val topBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.inversePrimary
    )

    when {
        currentDestination == Screen.Workout.route -> {
            CenterAlignedTopAppBar(
                title = { Text("Workout") },
                colors = topBarColors
            )
        }

        currentDestination == Screen.Diet.route -> {
            CenterAlignedTopAppBar(
                title = { Text("Diet") },
                colors = topBarColors
            )
        }

        currentDestination == Screen.Sleep.route -> {
            CenterAlignedTopAppBar(
                title = { Text("Sleep") },
                colors = topBarColors
            )
        }

        currentDestination == Screen.WorkoutGoal.route -> {
            CenterAlignedTopAppBar(
                title = { Text("Workout Goals") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = topBarColors
            )
        }

        currentDestination?.startsWith(Screen.WorkoutEdit.route) == true -> {
            CenterAlignedTopAppBar(
                title = { Text("Workout!") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = topBarColors
            )
        }

        else -> {
            CenterAlignedTopAppBar(
                title = { Text("HealthFusion") },
                colors = topBarColors
            )
        }
    }
}