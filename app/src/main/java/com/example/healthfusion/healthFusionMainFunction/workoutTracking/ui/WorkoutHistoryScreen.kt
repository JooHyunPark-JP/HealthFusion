package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedButton
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
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkOutName

@Composable
fun WorkoutHistoryScreen(
    viewModel: WorkoutViewModel,
    aerobicWorkouts: List<WorkOutName>,
    anaerobicWorkouts: List<WorkOutName>
) {
    val workouts by viewModel.workouts.collectAsState()

    var filterName by remember { mutableStateOf("") }
    var selectedAerobicWorkout by remember { mutableStateOf<WorkOutName?>(null) }
    var selectedAnaerobicWorkout by remember { mutableStateOf<WorkOutName?>(null) }
    var expandedAerobic by remember { mutableStateOf(false) }
    var expandedAnaerobic by remember { mutableStateOf(false) }


    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = filterName,
            onValueChange = { filterName = it },
            label = { Text("Filter by Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                OutlinedButton(
                    onClick = {
                        expandedAerobic = true
                        expandedAnaerobic = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = selectedAerobicWorkout?.name ?: "Select Aerobic Workout")
                }

                DropdownMenu(
                    expanded = expandedAerobic,
                    onDismissRequest = { expandedAerobic = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    aerobicWorkouts.forEach { workout ->
                        DropdownMenuItem(
                            text = { Text(workout.name) },
                            onClick = {
                                selectedAerobicWorkout = workout
                                selectedAnaerobicWorkout = null
                                expandedAerobic = false
                                filterName = ""
                            }
                        )
                    }

                    DropdownMenuItem(
                        text = { Text("Clear Selection") },
                        onClick = {
                            selectedAerobicWorkout = null
                            expandedAerobic = false
                        })
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                OutlinedButton(
                    onClick = {
                        expandedAnaerobic = true
                        expandedAerobic = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = selectedAnaerobicWorkout?.name ?: "Select Anaerobic Workout")
                }

                DropdownMenu(
                    expanded = expandedAnaerobic,
                    onDismissRequest = { expandedAnaerobic = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    anaerobicWorkouts.forEach { workout ->
                        DropdownMenuItem(
                            text = { Text(workout.name) },
                            onClick = {
                                selectedAnaerobicWorkout = workout
                                selectedAerobicWorkout = null
                                expandedAnaerobic = false
                                filterName = ""
                            }
                        )
                    }

                    DropdownMenuItem(
                        text = { Text("Clear Selection") },
                        onClick = {
                            selectedAnaerobicWorkout = null
                            expandedAnaerobic = false
                        })
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // show the filter data
        val filteredWorkouts = workouts.filter { workout ->
            (filterName.isEmpty() || workout.name.contains(filterName, ignoreCase = true)) &&
                    (selectedAerobicWorkout == null || workout.name == selectedAerobicWorkout?.name) &&
                    (selectedAnaerobicWorkout == null || workout.name == selectedAnaerobicWorkout?.name)
        }

        LazyColumn {
            items(filteredWorkouts) { workout ->
                Text(
                    text = "Workout: ${workout.name},\nDuration: ${workout.duration}, Calories Burned: ${workout.caloriesBurned}"
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}