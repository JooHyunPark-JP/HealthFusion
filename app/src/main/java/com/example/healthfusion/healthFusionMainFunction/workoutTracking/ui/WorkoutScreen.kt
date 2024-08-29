package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.google.firebase.auth.FirebaseAuth

@Composable
fun WorkoutScreen(viewModel: WorkoutViewModel, modifier: Modifier = Modifier) {
    val workouts by viewModel.workouts.collectAsState()

    var name by remember { mutableStateOf("") }
    var duration by remember { mutableStateOf("") }
    var caloriesBurned by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(WorkoutType.AEROBIC) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Workout Page", fontSize = 24.sp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp)
                .border(1.dp, Color.Gray)
                .clickable {
                    // Move to workout goal page logic here
                }
                .padding(16.dp)
        ) {
            Text(text = "Today's Workout Goals", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            //will change to progress bar for this text
            Text(text = "You can set your daily workout goals here.")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                //Move to workout goal page logic here
            }) {
                Text("Set Workout Goal")
            }
        }

        // 구분선 추가
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