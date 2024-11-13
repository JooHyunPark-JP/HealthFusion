package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.example.healthfusion.ui.theme.HealthFusionTheme


@Composable
fun WorkoutEdit(viewModel: WorkoutViewModel, workoutName: String, workoutType: WorkoutType) {

    val workouts by viewModel.workouts.collectAsState()

    var duration by remember { mutableStateOf("") }
    var caloriesBurned by remember { mutableStateOf("") }
    var distance by remember { mutableStateOf("") }
    var repetitions by remember { mutableStateOf("") }
    var set by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var type by remember { mutableStateOf(WorkoutType.AEROBIC) }

    var averageSpeed by remember { mutableIntStateOf(0) }
    var workoutDate by remember { mutableStateOf("") }

    val context = LocalContext.current

    HealthFusionTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = workoutName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF5F4)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
            ) {
                when (workoutType) {
                    WorkoutType.AEROBIC -> AerobicInputFields(
                        duration = duration,
                        distance = distance,
                        caloriesBurned = caloriesBurned,
                        onDurationChange = { duration = it },
                        onDistanceChange = { distance = it },
                        onCaloriesChange = { caloriesBurned = it }
                    )

                    WorkoutType.ANAEROBIC -> AnaerobicInputFields(
                        sets = set,
                        repetitions = repetitions,
                        weights = weight,
                        onSetsChange = { set = it },
                        onRepsChange = { repetitions = it },
                        onWeightsChange = { weight = it }
                    )
                }


                /*            LazyColumn {
                            items(workouts) { workout ->
                                Text(
                                    text = "Workout: ${workout.name}, Duration: ${workout.duration}, Calories Burned: ${workout.caloriesBurned}, Type: ${workout.type}"
                                )
                            }
                        }*/
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.addWorkout(
                        name = workoutName,
                        type = workoutType,

                        duration = duration.toIntOrNull(),
                        distance = distance.toIntOrNull(),
                        caloriesBurned = caloriesBurned.toIntOrNull(),

                        set = set.toIntOrNull(),
                        repetition = repetitions.toIntOrNull(),
                        weight = weight.toIntOrNull()
                    )

                    Toast.makeText(
                        context,
                        "New $workoutName data has been created!",
                        Toast.LENGTH_LONG
                    ).show()

                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF23af92)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Workout", color = Color.White)
            }
        }
    }
}

@Composable
fun AerobicInputFields(
    duration: String,
    distance: String,
    caloriesBurned: String,
    onDurationChange: (String) -> Unit,
    onDistanceChange: (String) -> Unit,
    onCaloriesChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = duration,
            onValueChange = onDurationChange,
            label = { Text("Duration (minutes)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF1F1F1),
                focusedContainerColor = Color(0xFFE0E0E0),
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedLabelColor = Color.Gray,
                focusedLabelColor = Color(0xFF23af92)
            )
        )
        TextField(
            value = distance,
            onValueChange = onDistanceChange,
            label = { Text("Distance (km)") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF1F1F1),
                focusedContainerColor = Color(0xFFE0E0E0),
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedLabelColor = Color.Gray,
                focusedLabelColor = Color(0xFF23af92)
            )
        )
        TextField(
            value = caloriesBurned,
            onValueChange = onCaloriesChange,
            label = { Text("Calories Burned") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF1F1F1),
                focusedContainerColor = Color(0xFFE0E0E0),
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedLabelColor = Color.Gray,
                focusedLabelColor = Color(0xFF23af92)
            )
        )
    }
}

@Composable
fun AnaerobicInputFields(
    sets: String,
    repetitions: String,
    weights: String,
    onSetsChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    onWeightsChange: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
    ) {
        TextField(
            value = sets,
            onValueChange = onSetsChange,
            label = { Text("Sets") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF1F1F1),
                focusedContainerColor = Color(0xFFE0E0E0),
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedLabelColor = Color.Gray,
                focusedLabelColor = Color(0xFF23af92)
            )
        )
        TextField(
            value = repetitions,
            onValueChange = onRepsChange,
            label = { Text("Repetitions") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF1F1F1),
                focusedContainerColor = Color(0xFFE0E0E0),
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedLabelColor = Color.Gray,
                focusedLabelColor = Color(0xFF23af92)
            )
        )
        TextField(
            value = weights,
            onValueChange = onWeightsChange,
            label = { Text("Weights") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF1F1F1),
                focusedContainerColor = Color(0xFFE0E0E0),
                unfocusedTextColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedLabelColor = Color.Gray,
                focusedLabelColor = Color(0xFF23af92)
            )
        )
    }
}

