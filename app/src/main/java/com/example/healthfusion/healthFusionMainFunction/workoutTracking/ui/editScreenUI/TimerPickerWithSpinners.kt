package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.editScreenUI

import android.widget.NumberPicker
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.healthfusion.R
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.FieldInfo

@Composable
fun TimePickerWithSpinners(inputValues: MutableMap<FieldInfo, String>) {
    // States for hours, minutes, and seconds

    val durationSeconds = inputValues[FieldInfo.DURATION]?.toIntOrNull() ?: 0

    val selectedHour = durationSeconds / 3600
    val selectedMinute = (durationSeconds % 3600) / 60
    val selectedSecond = durationSeconds % 60

    fun updateDuration(newHour: Int, newMinute: Int, newSecond: Int) {
        val totalSeconds = newHour * 3600 + newMinute * 60 + newSecond
        inputValues[FieldInfo.DURATION] = totalSeconds.toString()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_timer),
                    contentDescription = "Duration",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Duration",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                )

                Text(
                    text = "%02dh %02dm %02ds".format(
                        selectedHour,
                        selectedMinute,
                        selectedSecond
                    ),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    ),
                    textAlign = TextAlign.End
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NumberPickerSpinner(
                    label = "Hour",
                    range = 0..23,
                    value = selectedHour,
                    onValueChange = { newHour ->
                        updateDuration(
                            newHour,
                            selectedMinute,
                            selectedSecond
                        )
                    }
                )

                NumberPickerSpinner(
                    label = "Minute",
                    range = 0..59,
                    value = selectedMinute,
                    onValueChange = { newMinute ->
                        updateDuration(
                            selectedHour,
                            newMinute,
                            selectedSecond
                        )
                    }
                )

                NumberPickerSpinner(
                    label = "Second",
                    range = 0..59,
                    value = selectedSecond,
                    onValueChange = { newSecond ->
                        updateDuration(
                            selectedHour,
                            selectedMinute,
                            newSecond
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun NumberPickerSpinner(
    label: String,
    range: IntRange,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        AndroidView(
            factory = { context ->
                NumberPicker(context).apply {
                    minValue = range.first
                    maxValue = range.last
                    wrapSelectorWheel = true
                }
            },
            update = { picker ->
                picker.value = value
                picker.setOnValueChangedListener { _, _, newVal ->
                    onValueChange(newVal)
                }
            },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .width(72.dp)
        )
    }
}