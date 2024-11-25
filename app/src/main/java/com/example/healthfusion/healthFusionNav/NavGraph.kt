package com.example.healthfusion.healthFusionNav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.healthfusion.healthFusionMainFunction.dietTracking.ui.DietScreen
import com.example.healthfusion.healthFusionMainFunction.dietTracking.ui.DietViewModel
import com.example.healthfusion.healthFusionMainFunction.login.ui.LoginViewModel
import com.example.healthfusion.healthFusionMainFunction.profile.ui.ProfileScreen
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.ui.SleepScreen
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.ui.SleepViewModel
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AnaerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.WorkoutEdit
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.WorkoutGoalScreen
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.WorkoutScreen
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.WorkoutViewModel
import com.example.healthfusion.util.DateFormatter

@Composable
fun NavGraph(
    navController: NavHostController,
    workoutViewModel: WorkoutViewModel,
    dietViewModel: DietViewModel,
    sleepViewModel: SleepViewModel,
    loginViewModel: LoginViewModel
) {
    NavHost(navController = navController, startDestination = Screen.Workout.route) {
        composable(Screen.Workout.route) {
            WorkoutScreen(
                navController,
                viewModel = workoutViewModel,
                dateFormatter = DateFormatter()
            )
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
        composable(Screen.Profile.route) {
            ProfileScreen(loginViewModel = loginViewModel)
        }

        composable(
            route = "${Screen.WorkoutEdit.route}/{workoutClass}/{workoutName}/{workoutType}",
            arguments = listOf(
                navArgument("workoutClass") { type = NavType.StringType },
                navArgument("workoutName") { type = NavType.StringType },
                navArgument("workoutType") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val workoutClass = backStackEntry.arguments?.getString("workoutClass")
            val workoutName = backStackEntry.arguments?.getString("workoutName")
            val workoutTypeString = backStackEntry.arguments?.getString("workoutType")
            val workoutType = workoutTypeString?.let { WorkoutType.valueOf(it) }

            val workoutEnum = when (workoutClass) {
                AerobicWorkout::class.simpleName -> AerobicWorkout.entries.find { it.name == workoutName }
                AnaerobicWorkout::class.simpleName -> AnaerobicWorkout.entries.find { it.name == workoutName }
                else -> null
            }

            if (workoutEnum != null && workoutType != null) {
                val resolvedWorkoutName = when (workoutEnum) {
                    is AerobicWorkout -> workoutEnum.workoutName
                    is AnaerobicWorkout -> workoutEnum.workoutName
                    else -> "Unknown Workout"
                }

                WorkoutEdit(
                    viewModel = workoutViewModel,
                    workoutName = resolvedWorkoutName,
                    workoutType = workoutType
                )
            }
        }
    }
}