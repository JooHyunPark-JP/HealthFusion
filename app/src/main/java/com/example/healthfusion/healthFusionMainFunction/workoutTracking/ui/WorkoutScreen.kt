package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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

    var name by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var caloriesBurned by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(WorkoutType.AEROBIC) }

    val dailyGoals by viewModel.dailyGoals.collectAsState()
    val weeklyGoals by viewModel.weeklyGoals.collectAsState()

    var selectedWorkoutTabIndex by remember { mutableIntStateOf(0) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    //Later, workout list will be connected and syncroinized to healthconnect.
    //For now, just using static list of workouts list.
    val aerobicWorkouts = listOf(
        WorkOutName("Running", R.drawable.ic_placeholder_icon),
        WorkOutName("Cycling", R.drawable.ic_placeholder_icon),
        WorkOutName("Walking", R.drawable.ic_placeholder_icon)
    )

    val anaerobicWorkouts = listOf(
        WorkOutName("PushUps", R.drawable.ic_placeholder_icon),
        WorkOutName("Squats", R.drawable.ic_placeholder_icon)
    )

    val tabWorkoutPageTitles = listOf("Goals", "Workout List", "History")
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

        if (selectedWorkoutTabIndex == 0) {
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

        if (selectedWorkoutTabIndex == 1) {

            TabRow(selectedTabIndex = selectedTabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title.toString()) }
                    )
                }
            }

            val selectedWorkouts = if (selectedTabIndex == 0) aerobicWorkouts else anaerobicWorkouts


            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(selectedWorkouts) { index, workout ->
                    WorkoutGridItem(workout = workout)
                }
            }
        }

        // History 화면 (미구현 상태)
        if (selectedWorkoutTabIndex == 2) {
            Text(text = "Workout History will be implemented here.")
        }

        //Below here, these codes for later use.
        HorizontalDivider(
            color = Color.Gray,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 16.dp)
        )


        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Workout Name") })
        TextField(
            value = duration,
            onValueChange = { duration = it },
            label = { Text("Duration (minutes)") })
        TextField(
            value = caloriesBurned,
            onValueChange = { caloriesBurned = it },
            label = { Text("Calories Burned") })

        Row(verticalAlignment = Alignment.CenterVertically) {
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
            viewModel.addWorkout(
                name = name,
                duration = duration.toIntOrNull() ?: 0,
                caloriesBurned = caloriesBurned.toIntOrNull() ?: 0,
                type = type
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

        //temporary sign out function with button
        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            FirebaseAuth.getInstance().signOut()
        }) {
            Text("Sign Out")
        }
    }
}

@Composable
fun WorkoutGridItem(workout: WorkOutName) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .clickable { /* 운동 선택 시 처리할 동작 추가 */ },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = workout.imageResource),
            contentDescription = workout.name,
            modifier = Modifier
                .size(100.dp)
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