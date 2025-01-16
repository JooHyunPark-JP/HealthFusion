package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthfusion.R
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AnaerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.FieldInfo
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.Workout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoal
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoalType
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.example.healthfusion.healthFusionNav.Screen
import com.example.healthfusion.util.DateFormatter

@Composable
fun WorkoutScreen(
    navController: NavController,
    viewModel: WorkoutViewModel,
    modifier: Modifier = Modifier,
    dateFormatter: DateFormatter
) {
    val workouts by viewModel.workouts.collectAsState()

    val dailyGoals by viewModel.dailyGoals.collectAsState()
    val weeklyGoals by viewModel.weeklyGoals.collectAsState()

    var selectedWorkoutTabIndex by remember { mutableIntStateOf(0) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabWorkoutPageTitles = listOf("Goals", "Workout", "History", "Calendar")
    val tabTitles = listOf(WorkoutType.AEROBIC, WorkoutType.ANAEROBIC)

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TabRow(selectedTabIndex = selectedWorkoutTabIndex) {
            tabWorkoutPageTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedWorkoutTabIndex == index,
                    onClick = { selectedWorkoutTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedWorkoutTabIndex) {
            0 -> {
                WorkoutGoalBox(
                    navController = navController,
                    dailyGoals = dailyGoals,
                    weeklyGoals = weeklyGoals
                )


                Spacer(modifier = Modifier.height(8.dp))

                if (workouts.isNotEmpty()) {
                    Text(
                        text = "Your last 3 workouts activities.",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        val sortedByDescent = workouts.sortedByDescending { it.workoutDate }.take(3)
                        items(sortedByDescent.size) { index ->
                            val workout = sortedByDescent[index]
                            WorkoutRecentActivityBox(
                                workout = workout,
                                dateFormatter = dateFormatter
                            )
                        }

                    }
                } else {
                    Text(
                        text = "Your last 3 workouts activity: None!",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }


            }

            1 -> {
                Spacer(modifier = Modifier.height(8.dp))
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabTitles.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(title.toString()) }
                        )
                    }
                }

                val selectedWorkouts = if (selectedTabIndex == 0) {
                    AerobicWorkout.entries
                } else {
                    AnaerobicWorkout.entries
                }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(selectedWorkouts) { _, workout ->
                        WorkoutGridItem(
                            workout = workout,
                            navController = navController,
                        )
                    }
                }
            }

            2 -> {
                WorkoutHistoryScreen(
                    viewModel = viewModel,
                    dateFormatter = dateFormatter
                )
            }

            3 -> {
                WorkoutCalendarScreen(
                    viewModel = viewModel,
                )
            }

            else -> {
                Text(text = "Invalid Tab")
            }
        }
    }
}


@Composable
fun WorkoutGoalBox(
    navController: NavController,
    dailyGoals: List<WorkoutGoal>,
    weeklyGoals: List<WorkoutGoal>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(1.dp, Color.Gray)
            .clickable {
                navController.navigate(Screen.WorkoutGoal.route)
            }
            .padding(16.dp)
    ) {
        Text(text = "Workout Goals", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = "Your workout goal progress:")
        WorkoutGoalProgressBar(dailyGoals, WorkoutGoalType.DAILY)
        WorkoutGoalProgressBar(weeklyGoals, WorkoutGoalType.WEEKLY)

        Button(onClick = {
            navController.navigate(Screen.WorkoutGoal.route)
        }) {
            Text("Set Workout Goal")
        }
    }
}

@Composable
fun WorkoutRecentActivityBox(workout: Workout, dateFormatter: DateFormatter) {

    val workoutEnum = when (workout.type) {
        WorkoutType.AEROBIC -> AerobicWorkout.entries.find { it.workoutName == workout.name }
        WorkoutType.ANAEROBIC -> AnaerobicWorkout.entries.find { it.workoutName == workout.name }
    }

    val fields = when (workoutEnum) {
        is AerobicWorkout -> workoutEnum.fields
        is AnaerobicWorkout -> workoutEnum.fields
        else -> emptyList()
    }

    val imageResource = when (workoutEnum) {
        is AerobicWorkout -> workoutEnum.imageResource
        is AnaerobicWorkout -> workoutEnum.imageResource
        else -> R.drawable.ic_placeholder_icon
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    text = workout.name.replace("_", " "),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF212121)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Date: ${dateFormatter.simpleDateFormatWithoutSpecificTime(workout.workoutDate)}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                fields.forEach { fieldInfo ->
                    val formattedDuration =
                        workout.duration?.toLong()?.let { formatSecondsDuration(it) }
                    val value = when (fieldInfo) {
                        FieldInfo.DURATION -> formattedDuration.toString() ?: "N/A"
                        FieldInfo.DISTANCE -> workout.distance?.toString() ?: "N/A"
                        FieldInfo.CALORIES_BURNED -> workout.caloriesBurned?.toString() ?: "N/A"
                        FieldInfo.SETS -> workout.set?.toString() ?: "N/A"
                        FieldInfo.REPETITIONS -> workout.repetition?.toString() ?: "N/A"
                        FieldInfo.WEIGHTS -> workout.weight?.toString() ?: "N/A"
                        FieldInfo.TIMER -> null
                    }

                    if (!value.isNullOrEmpty()) {
                        Text(
                            text = "${fieldInfo.label}: $value",
                            fontSize = 14.sp,
                            color = Color(0xFF616161)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = imageResource),
                contentDescription = null,
                modifier = Modifier
                    .heightIn(max = 120.dp)
                    .widthIn(max = 120.dp)
                    .aspectRatio(1f),
                alignment = Alignment.CenterEnd
            )
        }
    }
}

@Composable
fun WorkoutGridItem(workout: Enum<*>, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                navController.navigate(
                    "${Screen.WorkoutEdit.route}/${workout.javaClass.simpleName}/${workout.name}/${
                        when (workout) {
                            is AerobicWorkout -> workout.workoutType.name
                            is AnaerobicWorkout -> workout.workoutType.name
                            else -> WorkoutType.AEROBIC.name
                        }
                    }"
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(
                id = when (workout) {
                    is AerobicWorkout -> workout.imageResource
                    is AnaerobicWorkout -> workout.imageResource
                    else -> R.drawable.ic_placeholder_icon
                }
            ),
            contentDescription = (workout as? AerobicWorkout)?.workoutName
                ?: (workout as? AnaerobicWorkout)?.workoutName,
            modifier = Modifier
                .size(200.dp)
                .padding(8.dp)
        )
        Text(
            text = when (workout) {
                is AerobicWorkout -> workout.workoutName.replace("_", " ")
                is AnaerobicWorkout -> workout.workoutName.replace("_", " ")
                else -> "Unknown"
            },
            fontSize = 16.sp
        )
    }
}


@Composable
private fun WorkoutGoalProgressBar(
    workoutGoal: List<WorkoutGoal>,
    goalType: WorkoutGoalType
) {
    val completedGoals = workoutGoal.count { it.isCompleted }
    val totalGoals = workoutGoal.size

    val calculatedProgress =
        if (totalGoals == 0) 0f else completedGoals / totalGoals.toFloat()

    val goalTypeText = goalType.toString().lowercase()

    LinearProgressIndicator(
        progress = { calculatedProgress },
        modifier = Modifier.fillMaxWidth(),
    )
    Text(text = "$completedGoals of $totalGoals $goalTypeText workout goal completed")
}