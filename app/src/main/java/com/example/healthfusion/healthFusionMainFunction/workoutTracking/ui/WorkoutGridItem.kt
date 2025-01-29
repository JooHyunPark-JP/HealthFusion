package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthfusion.R
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AnaerobicWorkout

@Composable
fun WorkoutGridItem(
    workout: Enum<*>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .clickable(onClick = onClick),
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