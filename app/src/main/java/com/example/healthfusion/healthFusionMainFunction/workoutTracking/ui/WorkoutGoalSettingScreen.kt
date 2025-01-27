package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoalType
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.example.healthfusion.healthFusionNav.Screen
import com.example.healthfusion.ui.theme.HealthFusionTheme

@Composable
fun WorkoutGoalSettingScreen(
    workoutViewModel: WorkoutViewModel,
    workoutName: String?,
    navController: NavController,
    goalType: WorkoutGoalType
) {
    //val workoutGoalDetails by workoutViewModel.workoutGoalDetails.collectAsState()

    val workoutGoalDetails by workoutViewModel.getGoalDetailsByType(goalType).collectAsState()

    var goalFrequency by remember { mutableStateOf("") }

    //  val currentWeekGoals by workoutViewModel.currentWeekGoals.collectAsState()

    // Check if the workoutName and goalType combination already exists
    val isGoalAlreadySet = workoutGoalDetails.any {
        it.workoutName == workoutName && it.goalPeriod == goalType.name.lowercase()
    }

    HealthFusionTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Set your ${goalType.name.lowercase()} goal for $workoutName",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            TextField(
                value = goalFrequency,
                onValueChange = { goalFrequency = it },
                label = { Text("How many times per ${goalType.name.lowercase()}?") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
               // enabled = !isGoalAlreadySet
            )

            Button(onClick = {
                workoutViewModel.addWorkoutGoalDetail(
                    workoutName = workoutName ?: "Workout name error",
                    workoutType = WorkoutType.AEROBIC, // for now just aerobic
                    goalFrequency = goalFrequency.toIntOrNull() ?: 0,
                    goalPeriod = goalType.name.lowercase()
                )

/*                when (goalType.name.lowercase()) {
                    "daily" -> workoutViewModel.addWorkoutGoal(
                        text = "Do $workoutName ${goalFrequency.toIntOrNull() ?: 0} times per ${goalType.name.lowercase()}",
                    )

                    "weekly" -> workoutViewModel.addWeeklyGoal(
                        text = "Do $workoutName ${goalFrequency.toIntOrNull() ?: 0} times per ${goalType.name.lowercase()}",
                    )
                }*/

                navController.navigate(Screen.WorkoutGoal.route) {
                    popUpTo(Screen.WorkoutGoalList.route) { inclusive = true }
                }
            }, /*enabled = !isGoalAlreadySet*/) {
                Text("Save Goal")
            }

            if (isGoalAlreadySet) {
                Text(
                    text = "A ${goalType.name.lowercase()} goal for $workoutName is already set.",
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }


            val filteredGoals = workoutGoalDetails.filter { it.workoutName == workoutName }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredGoals) { goal ->
                    Text(
                        text = "Goal: ${goal.goalFrequency} times (Progress: ${goal.currentProgress}), period: ${goal.goalPeriod}",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}