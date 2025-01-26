package com.example.healthfusion.healthFusionNav

sealed class Screen(val route: String) {
    data object Workout : Screen("workout")
    data object Diet : Screen("diet")
    data object Sleep : Screen("sleep")
    data object Profile : Screen("profile")
    data object WorkoutGoal : Screen("workoutGoal")
    data object WorkoutEdit : Screen("workoutEdit")
    data object WorkoutGoalList : Screen("workoutGoalList")

    data object WorkoutGoalSetting : Screen("workoutGoalSetting/{workoutName}") {
        fun createRoute(workoutName: String): String = "workoutGoalSetting/$workoutName"
    }
}