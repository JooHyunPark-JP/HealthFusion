package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AnaerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.FieldInfo
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.Workout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.example.healthfusion.util.DateFormatter

@Composable
fun WorkoutItem(
    workout: Workout,
    dateFormatter: DateFormatter,
    showWorkoutName: Boolean? = null,
    onDeleteClick: () -> Unit
) {
    val workoutEnum = when (workout.type) {
        WorkoutType.AEROBIC -> AerobicWorkout.entries.find { it.workoutName == workout.name }
        WorkoutType.ANAEROBIC -> AnaerobicWorkout.entries.find { it.workoutName == workout.name }
        else -> null
    }

    val fields = when (workoutEnum) {
        is AerobicWorkout -> workoutEnum.fields
        is AnaerobicWorkout -> workoutEnum.fields
        else -> emptyList()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(4.dp, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left side: workout details
            Column {
                if (showWorkoutName == true) {
                    Text(
                        text = workout.name,
                        fontSize = 16.sp,
                    )
                } else {
                    Text(
                        text = "Date: ${dateFormatter.simpleDateFormatWithoutSpecificTime(workout.workoutDate)}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))
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
                        FieldInfo.EQUIPMENT -> null
                        FieldInfo.GRIP_STYLE -> null
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
            // Right side: delete button
            IconButton(onClick = onDeleteClick) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }
}

@SuppressLint("DefaultLocale")
fun formatSecondsDuration(seconds: Long): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return String.format("%02dh %02dm %02ds", h, m, s)
}