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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkOutName
import com.example.healthfusion.util.DateFormatter
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import java.time.Instant
import java.time.ZoneId


@Composable
fun WorkoutHistoryScreen(
    viewModel: WorkoutViewModel,
    aerobicWorkouts: List<WorkOutName>,
    anaerobicWorkouts: List<WorkOutName>
) {
    val workouts by viewModel.workouts.collectAsState()

    val dateFormatter = DateFormatter()

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
                    Text(text = selectedAerobicWorkout?.name ?: "Select Aerobic")
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
                    Text(text = selectedAnaerobicWorkout?.name ?: "Select Anaerobic")
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


        // Get a list of x-axis labels as dates
        val dateLabels = filteredWorkouts.map { workout ->
            // Convert workout date to day-of-month for the x-axis
            val localDate = Instant.ofEpochMilli(workout.workoutDate)
                .atZone(ZoneId.systemDefault()).toLocalDate()
            localDate.dayOfMonth.toString()
        }


        val lineData = filteredWorkouts.mapIndexed { index, workout ->
            workout.caloriesBurned.toDouble() // This will be Y-axis data
        }

        LineChart(
            data = listOf(
                Line(
                    label = "Calories Burned",
                    values = lineData, // Pass the y-axis values (calories burned)
                    color = SolidColor(Color(0xFF23af92)), // Line color
                    curvedEdges = true, // Curved lines
                    dotProperties = DotProperties(
                        enabled = true, // Enable dots on the line
                        color = SolidColor(Color.White),
                        strokeWidth = 3.dp,
                        radius = 5.dp,
                        strokeColor = SolidColor(Color(0xFF23af92))
                    ),
                    popupProperties = PopupProperties(
                        enabled = true,
                        containerColor = Color(0xFF23af92), // Background color of the popup
                        textStyle = TextStyle.Default.copy(fontSize = 12.sp),
                        cornerRadius = 6.dp, // Rounded corners
                        contentBuilder = { value ->
                            // Display workout information based on the y-axis value (calories burned)
                            // Tolerance for floating-point comparison
                            val tolerance = 0.01

                            // Display workout information based on the y-axis value (calories burned)
                            val workout = filteredWorkouts.find {
                                kotlin.math.abs(it.caloriesBurned.toDouble() - value) < tolerance
                            }

                            workout?.let {
                                "Workout: ${it.name}\nCalories: ${it.caloriesBurned}\nDuration: ${it.duration} mins"
                            }.toString()
                        })
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            labelProperties = LabelProperties(
                enabled = true,
                textStyle = TextStyle.Default.copy(
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                ), // Customize the text style
                padding = 12.dp, // Add padding around the labels
                labels = dateLabels, // X-axis labels
                rotationDegreeOnSizeConflict = -45f, // Rotate labels if space conflicts
                forceRotation = false // If true, always rotate the labels
            )
        )

        LazyColumn {
            items(filteredWorkouts) { workout ->
                val formattedDate = dateFormatter.formatMillisToDateTime(workout.workoutDate)
                Text(
                    text = "Workout: ${workout.name},\nDuration: ${workout.duration}, Calories Burned: ${workout.caloriesBurned}\nDate: $formattedDate"
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}