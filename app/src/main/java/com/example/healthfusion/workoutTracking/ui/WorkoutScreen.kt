package com.example.healthfusion.workoutTracking.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healthfusion.workoutTracking.data.Workout
import com.example.healthfusion.workoutTracking.data.WorkoutType

@Composable
fun WorkoutScreen(viewModel: WorkoutViewModel, modifier: Modifier) {
    val workouts by viewModel.workouts.collectAsState()

    var name by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var caloriesBurned by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(WorkoutType.AEROBIC) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(value = name, onValueChange = { name = it }, label = { Text("Workout Name") })
        TextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duration (minutes)") })
        TextField(
            value = caloriesBurned,
            onValueChange = { caloriesBurned = it },
            label = { Text("Calories Burned") })

        Row {
            RadioButton(
                selected = type == WorkoutType.AEROBIC,
                onClick = { type = WorkoutType.AEROBIC }
            )
            Text("Aerobic")
            RadioButton(
                selected = type == WorkoutType.ANAEROBIC,
                onClick = { type = WorkoutType.ANAEROBIC }
            )
            Text("Anaerobic")
        }

        Button(onClick = {
            val workout = Workout(
                name = name,
                duration = duration.toIntOrNull() ?: 0,
                caloriesBurned = caloriesBurned.toIntOrNull() ?: 0,
                type = type
            )
            viewModel.addWorkout(workout)
        }) {
            Text("Add Workout")
        }

        LazyColumn {
            items(workouts) { workout ->
                Text(
                    text = "Workout: ${workout.name}, Duration: ${workout.duration}, Calories Burned: ${workout.caloriesBurned}, Type: ${workout.type}"
                )
            }
        }
    }
}