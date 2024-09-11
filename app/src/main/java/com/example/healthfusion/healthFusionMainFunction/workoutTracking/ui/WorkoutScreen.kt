package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Workout Page", fontSize = 24.sp)

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.Gray)
                .clickable {
                    // Move to workout goal page logic here
                    navController.navigate(Screen.WorkoutGoal.route)
                }
                .padding(16.dp)
        ) {
            Text(text = "Workout Goals", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            //will change to progress bar for this text
            Text(text = "Your workout goal progress:")
            WorkoutGoalProgressBar(dailyGoals, WorkoutGoalType.DAILY)
            WorkoutGoalProgressBar(weeklyGoals, WorkoutGoalType.WEEKLY)

            Button(onClick = {
                navController.navigate(Screen.WorkoutGoal.route)
            }) {
                Text("Set Workout Goal")
            }
        }

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