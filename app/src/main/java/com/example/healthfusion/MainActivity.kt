package com.example.healthfusion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.healthfusion.healthFusionMainFunction.dietTracking.ui.DietScreen
import com.example.healthfusion.healthFusionMainFunction.dietTracking.ui.DietViewModel
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.ui.SleepScreen
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.ui.SleepViewModel
import com.example.healthfusion.ui.theme.HealthFusionTheme
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.WorkoutScreen
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.WorkoutViewModel
import com.example.healthfusion.healthFusionNav.BottomNavBar
import com.example.healthfusion.healthFusionNav.NavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val workoutViewModel: WorkoutViewModel by viewModels()
    private val dietViewModel: DietViewModel by viewModels()
    private val sleepViewModel: SleepViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HealthFusionTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavBar(navController = navController) }) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        NavGraph(
                            navController = navController,
                            workoutViewModel = workoutViewModel,
                            dietViewModel = dietViewModel,
                            sleepViewModel = sleepViewModel
                        )
                    }
                }
            }
        }
    }
}

/*

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HealthFusionTheme {
        Greeting("Android")
    }
}*/
