package com.example.healthfusion.healthFusionNav

import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoalType

sealed class Screen(val route: String) {
    data object Workout : Screen("workout")
    data object Diet : Screen("diet")
    data object Sleep : Screen("sleep")
    data object Profile : Screen("profile")
    data object WorkoutGoal : Screen("workoutGoal")
    data object WorkoutEdit : Screen("workoutEdit")

    data object WorkoutGoalList : Screen("workoutGoalList/{goalType}") {
        fun createRoute(goalType: WorkoutGoalType): String = "workoutGoalList/${goalType.name}"
    }

    data object WorkoutGoalSetting : Screen("workoutGoalSetting/{workoutName}/{goalType}") {
        fun createRoute(workoutName: String, goalType: WorkoutGoalType): String =
            "workoutGoalSetting/$workoutName/${goalType.name}"
    }
}