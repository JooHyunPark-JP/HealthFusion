package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui


import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.healthfusion.R
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AnaerobicWorkout
import com.example.healthfusion.util.DateFormatter
import io.github.boguszpawlowski.composecalendar.Calendar
import io.github.boguszpawlowski.composecalendar.rememberCalendarState
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun WorkoutCalendarScreen(
    viewModel: WorkoutViewModel
) {
    val workouts by viewModel.workouts.collectAsState()

    var selectedAerobicWorkout by remember { mutableStateOf<AerobicWorkout?>(null) }
    var selectedAnaerobicWorkout by remember { mutableStateOf<AnaerobicWorkout?>(null) }
    var expandedAerobic by remember { mutableStateOf(false) }
    var expandedAnaerobic by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val calendarState = rememberCalendarState(
        minMonth = YearMonth.now().minusMonths(12),
        maxMonth = YearMonth.now().plusMonths(12),
        initialMonth = YearMonth.now()
    )

    val currentMonth = calendarState.monthState.currentMonth


    val workoutsForSelectedDate = selectedDate?.let { date ->
        workouts.filter { workout ->
            Instant.ofEpochMilli(workout.workoutDate).atZone(ZoneId.systemDefault())
                .toLocalDate() == date
        }
    } ?: emptyList()

    Column(modifier = Modifier.padding(8.dp)) {

        // show the filter data
        val filteredWorkouts = workouts.filter { workout ->
            (selectedAerobicWorkout == null || workout.name == selectedAerobicWorkout?.workoutName) &&
                    (selectedAnaerobicWorkout == null || workout.name == selectedAnaerobicWorkout?.workoutName)
        }

        val workoutDates = filteredWorkouts.map { workout ->
            Instant.ofEpochMilli(workout.workoutDate).atZone(ZoneId.systemDefault()).toLocalDate()
        }

        Box(modifier = Modifier.padding(8.dp)) {
            // Compose Calendar: External library
            Calendar(
                calendarState = calendarState,
                dayContent = { day ->
                    if (day.date.month == currentMonth.month) {
                        val isWorkoutDay = workoutDates.contains(day.date)
                        Box(
                            modifier = Modifier
                                .clickable { selectedDate = day.date }
                        ) {
                            DayContent(day = day.date, isWorkoutDay = isWorkoutDay)
                        }
                    } else {
                        Box(modifier = Modifier.size(48.dp))
                    }
                }
            )
        }

        // Dropdown for Aerobic Workout Selection
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
                    Text(
                        text = selectedAerobicWorkout?.workoutName?.replace("_", " ")
                            ?: "Select Aerobic"
                    )
                }

                DropdownMenu(
                    expanded = expandedAerobic,
                    onDismissRequest = { expandedAerobic = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AerobicWorkout.entries.forEach { workout ->
                        DropdownMenuItem(
                            text = { Text(workout.workoutName.replace("_", " ")) },
                            onClick = {
                                selectedAerobicWorkout = workout
                                selectedAnaerobicWorkout = null
                                expandedAerobic = false
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

            // Dropdown for Anaerobic Workout Selection
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
                    Text(
                        text = selectedAnaerobicWorkout?.workoutName?.replace("_", " ")
                            ?: "Select Anaerobic"
                    )
                }

                DropdownMenu(
                    expanded = expandedAnaerobic,
                    onDismissRequest = { expandedAnaerobic = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AnaerobicWorkout.entries.forEach { workout ->
                        DropdownMenuItem(
                            text = { Text(workout.workoutName.replace("_", " ")) },
                            onClick = {
                                selectedAnaerobicWorkout = workout
                                selectedAerobicWorkout = null
                                expandedAnaerobic = false
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

        // Display selected date
        selectedDate?.let { date ->
            Text(
                text = "All workout activities on: ${date.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))}",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
            )
        }

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            if (workoutsForSelectedDate.isEmpty()) {
                item {
                    Text(
                        text = "No workouts for this date",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(workoutsForSelectedDate) { workout ->
                    WorkoutItem(
                        workout = workout,
                        showWorkoutName = true,
                        dateFormatter = DateFormatter(), // Pass your DateFormatter instance
                        onDeleteClick = { viewModel.deleteWorkout(workout) }
                    )

                }
            }
        }
    }
}

@Composable
fun DayContent(day: LocalDate, isWorkoutDay: Boolean) {

    var isBlinking by remember { mutableStateOf(false) }

    val backgroundColor by animateColorAsState(
        targetValue = if (isWorkoutDay && isBlinking) colorResource(R.color.light_peach) else Color.Transparent,
        animationSpec = repeatable(
            iterations = 2,
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    LaunchedEffect(isWorkoutDay) {
        if (isWorkoutDay) {
            isBlinking = true
            delay(2000)
            isBlinking = false
        }
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .border(
                width = 3.dp,
                color = if (isWorkoutDay && !isBlinking) colorResource(R.color.light_peach) else backgroundColor,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text = day.dayOfMonth.toString())
    }


}