package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.editScreenUI

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthfusion.R
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.FieldInfo
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.WorkoutField
import kotlinx.coroutines.delay

@Composable
fun TimerComponent(
    inputValues: MutableMap<FieldInfo, String>
) {
    var elapsedSeconds by remember { mutableIntStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }


    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (isRunning) {
                delay(1000L)
                elapsedSeconds += 1
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = formatTimer(elapsedSeconds),
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            ),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Button(
                onClick = {
                    if (isRunning) {
                        inputValues[FieldInfo.DURATION] = elapsedSeconds.toString()
                    }
                    isRunning = !isRunning
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(if (isRunning) "Pause" else "Start")
            }

            // Reset
            OutlinedButton(
                onClick = {
                    isRunning = false
                    elapsedSeconds = 0
                    inputValues[FieldInfo.DURATION] = "0"
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Reset")
            }
        }
    }
}


@SuppressLint("DefaultLocale")
private fun formatTimer(totalSeconds: Int): String {
    val h = totalSeconds / 3600
    val m = (totalSeconds % 3600) / 60
    val s = totalSeconds % 60

    return if (h > 0) {
        String.format("%02d:%02d:%02d", h, m, s)
    } else {
        String.format("%02d:%02d", m, s)
    }
}

@Composable
fun TimerComponentWithToggle(
    field: WorkoutField,
    inputValues: MutableMap<FieldInfo, String>
) {
    var showTimer by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF2F2F2)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTimer = !showTimer }
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_timer),
                    contentDescription = "Timer",
                    tint = MaterialTheme.colorScheme.primary
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = "Timer",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = if (showTimer)
                            "Tap to hide the timer"
                        else
                            "Tap to start a simple timer",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Text(
                    text = if (showTimer) "ON" else "OFF",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = if (showTimer)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        AnimatedVisibility(visible = showTimer) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                TimerComponent(inputValues = inputValues)
            }
        }
    }
}