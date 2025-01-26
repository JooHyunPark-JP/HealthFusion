package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.example.healthfusion.ui.theme.HealthFusionTheme

@Composable
fun WorkoutGoalSettingScreen(
    workoutViewModel: WorkoutViewModel,
    workoutName: String?
) {
    val workoutGoalDetails by workoutViewModel.workoutGoalDetails.collectAsState()
    var goalFrequency by remember { mutableStateOf("") }

    val currentWeekGoals by workoutViewModel.currentWeekGoals.collectAsState()

    HealthFusionTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Set your goal for $workoutName",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            TextField(
                value = goalFrequency,
                onValueChange = { goalFrequency = it },
                label = { Text("How many times per week?") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Button(onClick = {
                workoutViewModel.addWorkoutGoalDetail(
                    workoutName = workoutName ?: "Workout name error",
                    workoutType = WorkoutType.AEROBIC, // for now just aerobic
                    goalFrequency = goalFrequency.toIntOrNull() ?: 0,
                    goalPeriod = "weekly" //currently just weekly
                )
            }) {
                Text("Save Goal")
            }


            val filteredGoals = workoutGoalDetails.filter { it.workoutName == workoutName }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredGoals) { goal ->
                    Text(
                        text = "Goal: ${goal.goalFrequency} times/week (Progress: ${goal.currentProgress})",
                        fontSize = 16.sp
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(currentWeekGoals) { goal ->
                    Column {
                        Text(
                            text = "Workout: ${goal.workoutName}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        LinearProgressIndicator(
                            progress = { goal.currentProgress.toFloat() / goal.goalFrequency.toFloat() },
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Text(
                            text = "${goal.currentProgress} of ${goal.goalFrequency} completed",
                            fontSize = 14.sp
                        )
                    }
                }
            }

        }
    }
}