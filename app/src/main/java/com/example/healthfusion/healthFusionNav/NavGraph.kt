package com.example.healthfusion.healthFusionNav

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.healthfusion.healthFusionMainFunction.dietTracking.ui.DietScreen
import com.example.healthfusion.healthFusionMainFunction.dietTracking.ui.DietViewModel
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.ui.SleepScreen
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.ui.SleepViewModel
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.WorkoutGoalScreen
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.WorkoutScreen
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.WorkoutViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    workoutViewModel: WorkoutViewModel,
    dietViewModel: DietViewModel,
    sleepViewModel: SleepViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Workout.route) {
        composable(Screen.Workout.route) {
            WorkoutScreen(navController, viewModel = workoutViewModel)
        }
        composable(Screen.Diet.route) {
            DietScreen(viewModel = dietViewModel)
        }
        composable(Screen.Sleep.route) {
            SleepScreen(viewModel = sleepViewModel)
        }
        composable(Screen.WorkoutGoal.route) {
            WorkoutGoalScreen(viewModel = workoutViewModel)
        }
    }
}