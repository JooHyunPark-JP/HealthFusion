package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.Workout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun WorkoutHistoryScreen(
    viewModel: WorkoutViewModel,
    modifier: Modifier = Modifier
) {
    val workouts by viewModel.workouts.collectAsState()

    var filterName by remember { mutableStateOf("") }


    var selectedAerobicWorkout by remember { mutableStateOf<Workout?>(null) }
    var selectedAnaerobicWorkout by remember { mutableStateOf<Workout?>(null) }
    var expandedAerobic by remember { mutableStateOf(false) }
    var expandedAnaerobic by remember { mutableStateOf(false) }
    var showExtraOptions by remember { mutableStateOf(false) }
    var selectedDetailOption by remember { mutableStateOf<String?>(null) }
    var expandedDetailOptions by remember { mutableStateOf(false) }


    Column(modifier = Modifier.padding(16.dp)) {

        // show the filter data
        val filteredWorkouts = workouts.filter { workout ->
            (filterName.isEmpty() || workout.name.contains(filterName, ignoreCase = true)) &&
                    (selectedAerobicWorkout == null || workout.name == selectedAerobicWorkout?.name) &&
                    (selectedAnaerobicWorkout == null || workout.name == selectedAnaerobicWorkout?.name)
        }


        // Get the first and last workout dates for the x-axis labels
        val firstWorkoutDate = filteredWorkouts.minOfOrNull { it.workoutDate }
        val lastWorkoutDate = filteredWorkouts.maxOfOrNull { it.workoutDate }

        // Format the first and last dates for display
        val dateLabels = listOfNotNull(
            firstWorkoutDate?.let {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                    Date(
                        it
                    )
                )
            },
            lastWorkoutDate?.let {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                    Date(
                        it
                    )
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (filteredWorkouts.isNotEmpty()) {
            val lineData = filteredWorkouts.mapIndexed { _, workout ->
                workout.caloriesBurned.toDouble()
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f) // Fill 50% of the parent height
            ) {

                LineChart(
                    data = listOf(
                        Line(
                            label = "Calories Burned",
                            values = lineData,
                            color = SolidColor(Color(0xFF23af92)),
                            curvedEdges = true,
                            dotProperties = DotProperties(
                                enabled = true,
                                color = SolidColor(Color.White),
                                strokeWidth = 3.dp,
                                radius = 5.dp,
                                strokeColor = SolidColor(Color(0xFF23af92))
                            ),
                            popupProperties = PopupProperties(
                                enabled = true,
                                containerColor = Color(0xFF23af92),
                                textStyle = TextStyle.Default.copy(fontSize = 12.sp),
                                cornerRadius = 6.dp,
                                contentBuilder = { value ->
                                    val tolerance = 0.01
                                    val workout = filteredWorkouts.find {
                                        kotlin.math.abs(it.caloriesBurned.toDouble() - value) < tolerance
                                    }
                                    workout?.let {
                                        "Workout: ${it.name}\nCalories: ${it.caloriesBurned}\nDuration: ${it.duration} mins"
                                    } ?: "No data"
                                }
                            )
                        )
                    ),
                    modifier = Modifier
                        .fillMaxWidth(),
                    labelProperties = LabelProperties(
                        enabled = true,
                        textStyle = TextStyle.Default.copy(
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        ),
                        padding = 12.dp,
                        labels = dateLabels,
                        rotationDegreeOnSizeConflict = -45f,
                        forceRotation = false
                    )
                )
            }
        } else {
            Text(
                text = "No data available!",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                textAlign = TextAlign.Center
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = filterName,
            onValueChange = { filterName = it },
            label = { Text("Filter by Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            val aerobicWorkout =
                workouts.filter { it.type == WorkoutType.AEROBIC }.distinctBy { it.name }
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
                    if (aerobicWorkout.isNotEmpty())
                        aerobicWorkout.forEach { workout ->
                            DropdownMenuItem(
                                text = { Text(workout.name) },
                                onClick = {
                                    selectedAerobicWorkout = workout
                                    selectedAnaerobicWorkout = null
                                    expandedAerobic = false
                                    filterName = ""
                                    showExtraOptions = true
                                }
                            )
                        }
                    else {
                        DropdownMenuItem(
                            text = { Text("No Aerobic data in this account") },
                            onClick = { })
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                val anaerobicWorkout =
                    workouts
                        .filter { it.type == WorkoutType.ANAEROBIC }
                        .distinctBy { it.name }
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
                    if (anaerobicWorkout.isNotEmpty())
                        anaerobicWorkout.forEach { workout ->
                            DropdownMenuItem(
                                text = { Text(workout.name) },
                                onClick = {
                                    selectedAnaerobicWorkout = workout
                                    selectedAerobicWorkout = null
                                    expandedAnaerobic = false
                                    filterName = ""
                                    showExtraOptions = true
                                }
                            )
                        }
                    else {
                        DropdownMenuItem(
                            text = { Text("No anaerobicWorkout data in this account") },
                            onClick = { })
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        if (showExtraOptions) {
            Box {
                OutlinedButton(
                    onClick = {
                        expandedDetailOptions = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View More Options")
                }

                DropdownMenu(
                    expanded = expandedDetailOptions,
                    onDismissRequest = { expandedDetailOptions = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    DropdownMenuItem(
                        text = { Text("Calories Burned") },
                        onClick = {
                            selectedDetailOption = "Calories Burned"
                            expandedDetailOptions = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Distance") },
                        onClick = {
                            selectedDetailOption = "Distance"
                            expandedDetailOptions = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Duration") },
                        onClick = {
                            selectedDetailOption = "Duration"
                            expandedDetailOptions = false
                        }
                    )
                }
            }

            selectedDetailOption?.let {
                Text(
                    text = "Selected: $it",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = {
                selectedAerobicWorkout = null
                selectedAnaerobicWorkout = null
                expandedAerobic = false
                expandedAnaerobic = false
                showExtraOptions = false
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Clear filter")
        }
    }
}