package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.editScreenUI

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthfusion.R
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.FieldInfo
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.WorkoutField
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.formatSeconds

@Composable
fun TimerComponent(inputValues: MutableMap<FieldInfo, String>) {
    var elapsedTime by remember { mutableLongStateOf(0L) } // Elapsed time in seconds
    var isRunning by remember { mutableStateOf(false) } // Timer running state

    // Timer logic
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (true) {
                kotlinx.coroutines.delay(1000L) // Wait for 1 second
                elapsedTime++
            }
        }
    }

    // UI rendering
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Time: ${formatSeconds(elapsedTime)}",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { isRunning = true },
                enabled = !isRunning
            ) {
                Text("Start")
            }
            Button(
                onClick = { isRunning = false },
                enabled = isRunning
            ) {
                Text("Pause")
            }
            Button(
                onClick = {
                    elapsedTime = 0
                }
            ) {
                Text("Reset")
            }
        }
    }
}

@Composable
fun TimerComponentWithToggle(field: WorkoutField, inputValues: MutableMap<FieldInfo, String>) {
    var showTimer by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        // Text button to toggle the timer
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Icon(painterResource(id = R.drawable.ic_timer), contentDescription = "Add")
            Text(
                text = "Need timer? (Click)",
                style = TextStyle(
                    color = Color.Blue,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline,
                    fontSize = 16.sp
                ),
                modifier = Modifier
                    .clickable { showTimer = !showTimer }
                    .padding(start= 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Conditionally render the TimerComponent
        if (showTimer) {
            TimerComponent(inputValues = inputValues)
        }
    }
}