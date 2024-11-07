package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.Workout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.example.healthfusion.util.DateFormatter
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
    dateFormatter: DateFormatter,
    modifier: Modifier = Modifier
) {
    val workouts by viewModel.workouts.collectAsState()

    var selectedAerobicWorkout by remember { mutableStateOf<Workout?>(null) }
    var selectedAnaerobicWorkout by remember { mutableStateOf<Workout?>(null) }
    var expandedAerobic by remember { mutableStateOf(false) }
    var expandedAnaerobic by remember { mutableStateOf(false) }
    var selectedDetailOption by remember { mutableStateOf<String?>(null) }
    var expandedDetailOptions by remember { mutableStateOf(false) }

    var aerobicWorkoutTabIndex by remember { mutableIntStateOf(0) }
    val tabWorkoutPageTitles = listOf("Line Chart", "Workout List")

    Column(modifier = Modifier.padding(16.dp)) {

        val filteredWorkouts = workouts.filter { workout ->
            (selectedAerobicWorkout == null || workout.name == selectedAerobicWorkout?.name) &&
                    (selectedAnaerobicWorkout == null || workout.name == selectedAnaerobicWorkout?.name)
        }


        if (selectedAerobicWorkout != null) {
            TabRow(selectedTabIndex = aerobicWorkoutTabIndex) {
                tabWorkoutPageTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = aerobicWorkoutTabIndex == index,
                        onClick = { aerobicWorkoutTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
        }

        //if user choose Anaerobic workout, show the list
        if (selectedAnaerobicWorkout != null && filteredWorkouts.isNotEmpty()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Workout Name: ${filteredWorkouts.first().name}")
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    items(filteredWorkouts.size) { index ->
                        val sortedByDescent = filteredWorkouts.sortedByDescending { it.workoutDate }
                        val workout = sortedByDescent[index]
                        AnaerobicWorkoutItem(
                            workout = workout,
                            onDeleteClick = { viewModel.deleteWorkout(workout) },
                            dateFormatter = dateFormatter
                        )
                    }
                }
            }
            // if user choose Aerobic workout, show the line chart
        } else if (selectedAerobicWorkout != null && filteredWorkouts.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            when (aerobicWorkoutTabIndex) {
                0 -> {
                    val lineData
                            : List<Double> = when (selectedDetailOption) {
                        "Calories Burned" -> filteredWorkouts.map {
                            it.caloriesBurned?.toDouble() ?: 0.0
                        }

                        "Distance" -> filteredWorkouts.map { it.distance?.toDouble() ?: 0.0 }
                        "Duration" -> filteredWorkouts.map { it.duration?.toDouble() ?: 0.0 }
                        else -> filteredWorkouts.map { it.caloriesBurned?.toDouble() ?: 0.0 }
                    }

                    AerobicLineChart(
                        lineData,
                        selectedDetailOption,
                        dateLabels = getDateLabels(filteredWorkouts),
                        filteredWorkouts
                    )
                }

                1 -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Workout Name: ${filteredWorkouts.first().name}")
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            items(filteredWorkouts.size) { index ->
                                val sortedByDescent =
                                    filteredWorkouts.sortedByDescending { it.workoutDate }
                                val workout = sortedByDescent[index]
                                AerobicWorkoutItem(
                                    workout = workout,
                                    onDeleteClick = { viewModel.deleteWorkout(workout) },
                                    dateFormatter = dateFormatter
                                )
                            }
                        }
                    }

                }
            }
        } else {
            Text(
                text = "Please choose Workout Type",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.fillMaxWidth()) {
            val aerobicWorkout =
                workouts.filter { it.type == WorkoutType.AEROBIC }.distinctBy { it.name }
            Box(
                modifier = Modifier.weight(1f)
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
                    aerobicWorkout.forEach { workout ->
                        DropdownMenuItem(
                            text = { Text(workout.name) },
                            onClick = {
                                selectedAerobicWorkout = workout
                                selectedAnaerobicWorkout = null
                                expandedAerobic = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            val anaerobicWorkout =
                workouts.filter { it.type == WorkoutType.ANAEROBIC }.distinctBy { it.name }
            Box(
                modifier = Modifier.weight(1f)
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
                    anaerobicWorkout.forEach { workout ->
                        DropdownMenuItem(
                            text = { Text(workout.name) },
                            onClick = {
                                selectedAnaerobicWorkout = workout
                                selectedAerobicWorkout = null
                                expandedAnaerobic = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedAerobicWorkout != null) {
            Box {
                OutlinedButton(
                    onClick = {
                        expandedDetailOptions = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = selectedDetailOption ?: "View More Options")
                }
                DropdownMenu(
                    expanded = expandedDetailOptions,
                    onDismissRequest = { expandedDetailOptions = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("Calories Burned", "Distance", "Duration").forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedDetailOption = option
                                expandedDetailOptions = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AnaerobicWorkoutItem(
    workout: Workout,
    onDeleteClick: () -> Unit,
    dateFormatter: DateFormatter
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                "Date: ${dateFormatter.simpleDateFormatWithoutSpecificTime(workout.workoutDate)}"
            )
            Text("Sets: ${workout.set ?: "N/A"}")
            Text("Repetitions: ${workout.repetition ?: "N/A"}")
            Text("Weight: ${workout.weight ?: "N/A"} kg")
        }
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
        }
    }

}


@Composable
fun AerobicWorkoutItem(
    workout: Workout,
    onDeleteClick: () -> Unit,
    dateFormatter: DateFormatter
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                "Date: ${
                    dateFormatter.simpleDateFormatWithoutSpecificTime(workout.workoutDate)
                }"
            )
            Text("Duration: ${workout.duration ?: "N/A"} mins")
            Text("Distance: ${workout.distance ?: "N/A"} km")
            Text("Calories Burned: ${workout.caloriesBurned ?: "N/A"}")
        }
        IconButton(onClick = onDeleteClick) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
        }
    }
}

@Composable
fun AerobicLineChart(
    lineData: List<Double>,
    selectedDetailOption: String?,
    dateLabels: List<String>,
    filteredWorkouts: List<Workout>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
    ) {
        LineChart(
            data = listOf(
                Line(
                    label = selectedDetailOption ?: "Calories Burned",
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
                                when (selectedDetailOption) {
                                    "Calories Burned" -> kotlin.math.abs(
                                        (it.caloriesBurned?.toDouble()
                                            ?: 0.0) - value
                                    ) < tolerance

                                    "Duration" -> kotlin.math.abs(
                                        (it.duration?.toDouble()
                                            ?: 0.0) - value
                                    ) < tolerance

                                    "Distance" -> kotlin.math.abs(
                                        (it.distance?.toDouble()
                                            ?: 0.0) - value
                                    ) < tolerance

                                    else -> kotlin.math.abs(
                                        (it.caloriesBurned?.toDouble()
                                            ?: 0.0) - value
                                    ) < tolerance
                                }
                            }
                            workout?.let {
                                "Date: ${
                                    SimpleDateFormat(
                                        "yyyy-MM-dd",
                                        Locale.getDefault()
                                    ).format(Date(it.workoutDate))
                                }\nWorkout: ${it.name}\nCalories: ${it.caloriesBurned}\nDuration: ${it.duration} mins"
                            } ?: "No data"
                        }
                    )
                )
            ),
            modifier = Modifier.fillMaxWidth(),
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
}

fun getDateLabels(filteredWorkouts: List<Workout>): List<String> {
    val firstWorkoutDate = filteredWorkouts.minOfOrNull { it.workoutDate }
    val lastWorkoutDate = filteredWorkouts.maxOfOrNull { it.workoutDate }

    return listOfNotNull(
        firstWorkoutDate?.let {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
        },
        lastWorkoutDate?.let {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
        }
    )
}