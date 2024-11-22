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
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AnaerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.example.healthfusion.ui.theme.HealthFusionTheme

@Composable
fun WorkoutEdit(viewModel: WorkoutViewModel, workoutName: String, workoutType: WorkoutType) {

    val workouts by viewModel.workouts.collectAsState()

    val context = LocalContext.current

    val workoutEnum: Any? = when (workoutType) {
        WorkoutType.AEROBIC -> AerobicWorkout.entries.find { it.workoutName == workoutName }
        WorkoutType.ANAEROBIC -> AnaerobicWorkout.entries.find { it.workoutName == workoutName }
    }

    if (workoutEnum == null) {
        Text("Invalid Workout Type or Name")
        return
    }

    val fields = when (workoutEnum) {
        is AerobicWorkout -> workoutEnum.fields
        is AnaerobicWorkout -> workoutEnum.fields
        else -> emptyList()
    }

    val displayName = when (workoutEnum) {
        is AerobicWorkout -> workoutEnum.workoutName
        is AnaerobicWorkout -> workoutEnum.workoutName
        else -> "Unknown Workout"
    }

    val inputValues = remember { mutableStateMapOf<String, String>() }

    HealthFusionTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = displayName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF5F4)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
            ) {
                DynamicWorkoutInputFields(
                    fields = fields.map { fieldInfo ->
                        WorkoutField(
                            label = fieldInfo.label,
                            value = inputValues.getOrDefault(fieldInfo.label, ""),
                            onChange = { inputValues[fieldInfo.label] = it }
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.addWorkout(
                        name = displayName,
                        type = workoutType,
                        duration = inputValues["Duration (minutes)"]?.toIntOrNull(),
                        distance = inputValues["Distance (km)"]?.toIntOrNull(),
                        caloriesBurned = inputValues["Calories Burned"]?.toIntOrNull(),
                        set = inputValues["Sets"]?.toIntOrNull(),
                        repetition = inputValues["Repetitions"]?.toIntOrNull(),
                        weight = inputValues["Weights (kg)"]?.toIntOrNull()
                    )
                    Toast.makeText(
                        context,
                        "New $displayName data has been created!",
                        Toast.LENGTH_LONG
                    ).show()
                },
                modifier = Modifier
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
fun DynamicWorkoutInputFields(fields: List<WorkoutField>) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        fields.forEach { field ->
            TextField(
                value = field.value,
                onValueChange = field.onChange,
                label = { Text(field.label) },
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
}

data class WorkoutField(val label: String, val value: String, val onChange: (String) -> Unit)

