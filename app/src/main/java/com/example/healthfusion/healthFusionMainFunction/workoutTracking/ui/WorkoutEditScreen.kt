package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.example.healthfusion.ui.theme.HealthFusionTheme


@Composable
fun WorkoutEdit(viewModel: WorkoutViewModel, workoutName: String, workoutType: WorkoutType) {

    val workouts by viewModel.workouts.collectAsState()

    var duration by remember { mutableStateOf("") }
    var caloriesBurned by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }
    var repetitions by remember { mutableIntStateOf(0) }
    var sets by remember { mutableIntStateOf(0) }
    var weights by remember { mutableIntStateOf(0) }
    var type by remember { mutableStateOf(WorkoutType.AEROBIC) }

    var averageSpeed by remember { mutableIntStateOf(0) }
    var workoutDate by remember { mutableStateOf("") }

    HealthFusionTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = workoutName)
            Text(text = workoutType.name)

            when (workoutName) {
                "Running", "Cycling", "Walking" -> AerobicInputFields(
                    duration = duration,
                    distance = distance,
                    caloriesBurned = caloriesBurned,
                    onDurationChange = { duration = it },
                    onDistanceChange = { distance = it },
                    onCaloriesChange = { caloriesBurned = it }
                )

                "PushUps", "Squats" -> AnaerobicInputFields(
                    sets = sets,
                    repetitions = repetitions,
                    weights = weights,
                    onSetsChange = { sets = it },
                    onRepsChange = { repetitions = it },
                    onWeightsChange = { weights = it }
                )

                else -> {
                    Text("Unknown workout type.")
                }
            }

            Button(onClick = {
                viewModel.addWorkout(
                    name = workoutName,
                    duration = duration.toIntOrNull() ?: 0,
                    caloriesBurned = caloriesBurned.toIntOrNull() ?: 0,
                    type = workoutType
                )
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
}

@Composable
fun AerobicInputFields(
    duration: String,
    distance: String,
    caloriesBurned: String,
    onDurationChange: (String) -> Unit,
    onDistanceChange: (String) -> Unit,
    onCaloriesChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        TextField(
            value = duration,
            onValueChange = onDurationChange,
            label = { Text("Duration (minutes)") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 4.dp)
        )
        TextField(
            value = distance,
            onValueChange = onDistanceChange,
            label = { Text("Distance (km)") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 4.dp)
        )
        TextField(
            value = caloriesBurned,
            onValueChange = onCaloriesChange,
            label = { Text("Calories Burned") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 4.dp)
        )
    }
}

@Composable
fun AnaerobicInputFields(
    sets: Int,
    repetitions: Int,
    weights: Int,
    onSetsChange: (Int) -> Unit,
    onRepsChange: (Int) -> Unit,
    onWeightsChange: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {  // TextField 간격을 8dp로 설정
        TextField(
            value = sets.toString(),
            onValueChange = { onSetsChange(it.toIntOrNull() ?: 0) },
            label = { Text("Sets") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 4.dp)
        )
        TextField(
            value = repetitions.toString(),
            onValueChange = { onRepsChange(it.toIntOrNull() ?: 0) },
            label = { Text("Repetitions") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 4.dp)
        )
        TextField(
            value = weights.toString(),
            onValueChange = { onWeightsChange(it.toIntOrNull() ?: 0) },
            label = { Text("Weights") },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(horizontal = 4.dp)
        )
    }
}

