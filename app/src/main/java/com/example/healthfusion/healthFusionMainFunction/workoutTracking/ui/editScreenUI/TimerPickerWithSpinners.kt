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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.FieldInfo

@Composable
fun TimePickerWithSpinners(inputValues: MutableMap<FieldInfo, String>) {
    // States for hours, minutes, and seconds
    var selectedHour by remember { mutableIntStateOf(0) }
    var selectedMinute by remember { mutableIntStateOf(0) }
    var selectedSecond by remember { mutableIntStateOf(0) }

    fun updateDuration() {
        val totalSeconds = selectedHour * 3600 + selectedMinute * 60 + selectedSecond
        inputValues[FieldInfo.DURATION] = totalSeconds.toString()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hour Spinner
            NumberPickerSpinner(
                label = "Hour",
                range = 0..23,
                value = selectedHour,
                onValueChange = {
                    selectedHour = it
                    updateDuration()
                }
            )

            // Minute Spinner
            NumberPickerSpinner(
                label = "Minute",
                range = 0..59,
                value = selectedMinute,
                onValueChange = {
                    selectedMinute = it
                    updateDuration()
                }
            )

            // Second Spinner
            NumberPickerSpinner(
                label = "Second",
                range = 0..59,
                value = selectedSecond,
                onValueChange = {
                    selectedSecond = it
                    updateDuration()
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Display updated duration dynamically
        Text(
            text = "Workout Time: ${"%02d".format(selectedHour)}h ${
                "%02d".format(selectedMinute)
            }m ${"%02d".format(selectedSecond)}s",
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
fun NumberPickerSpinner(
    label: String,
    range: IntRange,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 14.sp, fontWeight = FontWeight.Bold)

        // Integrate Android's NumberPicker
        AndroidView(
            factory = { context ->
                NumberPicker(context).apply {
                    minValue = range.first
                    maxValue = range.last
                    wrapSelectorWheel = true // Allows cycling through values
                }
            },
            update = { picker ->
                picker.value = value
                picker.setOnValueChangedListener { _, _, newVal ->
                    onValueChange(newVal)
                }
            },
            modifier = Modifier.width(100.dp)
        )
    }
}