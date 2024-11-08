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
            route = "${Screen.WorkoutEdit.route}/{workoutName}/{workoutType}",
            arguments = listOf(
                navArgument("workoutName") { type = NavType.StringType },
                navArgument("workoutType") { type = NavType.StringType })
        ) { backStackEntry ->
            val workoutName = backStackEntry.arguments?.getString("workoutName")

            val workoutTypeString = backStackEntry.arguments?.getString("workoutType")
            val workoutType = workoutTypeString?.let { WorkoutType.valueOf(it) }

            WorkoutEdit(
                viewModel = workoutViewModel,
                workoutName = workoutName.orEmpty(),
                workoutType = workoutType ?: WorkoutType.AEROBIC
            )
        }
    }
}