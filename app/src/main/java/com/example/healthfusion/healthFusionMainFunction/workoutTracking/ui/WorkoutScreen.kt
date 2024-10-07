package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.healthfusion.R
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkOutName
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoal
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoalType
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.example.healthfusion.healthFusionNav.Screen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun WorkoutScreen(
    navController: NavController,
    viewModel: WorkoutViewModel,
    modifier: Modifier = Modifier
) {
    val workouts by viewModel.workouts.collectAsState()

    val dailyGoals by viewModel.dailyGoals.collectAsState()
    val weeklyGoals by viewModel.weeklyGoals.collectAsState()

    var selectedWorkoutTabIndex by remember { mutableIntStateOf(0) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    //Later, workout list will be connected and syncroinized to healthconnect API.
    //For now, just using static list of workouts list.
    val aerobicWorkouts = listOf(
        WorkOutName("Running", R.drawable.running_pose2),
        WorkOutName("Cycling", R.drawable.cycling_pose),
        WorkOutName("Walking", R.drawable.walking_pose)
    )

    val anaerobicWorkouts = listOf(
        WorkOutName("PushUps", R.drawable.pushup_pose),
        WorkOutName("Squats", R.drawable.squat_pose)
    )

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

                //temporary sign out function with button
                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = {
                    FirebaseAuth.getInstance().signOut()
                }) {
                    Text("Sign Out")
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

                val selectedWorkouts =
                    if (selectedTabIndex == 0) aerobicWorkouts else anaerobicWorkouts

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(selectedWorkouts) { index, workout ->
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
                    aerobicWorkouts = aerobicWorkouts,
                    anaerobicWorkouts = anaerobicWorkouts
                )
            }

            3 -> {
                WorkoutCalendarScreen(
                    viewModel = viewModel,
                    aerobicWorkouts = aerobicWorkouts,
                    anaerobicWorkouts = anaerobicWorkouts
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
fun WorkoutGridItem(workout: WorkOutName, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { navController.navigate("${Screen.WorkoutEdit.route}/${workout.name}") },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = workout.imageResource),
            contentDescription = workout.name,
            modifier = Modifier
                .size(200.dp)
                .padding(8.dp)
        )
        Text(text = workout.name, fontSize = 16.sp)
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