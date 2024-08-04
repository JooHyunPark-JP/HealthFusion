package com.example.healthfusion.healthFusionMainFunction.sleepTracking.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.Sleep

@Composable
fun SleepScreen(viewModel: SleepViewModel, modifier: Modifier = Modifier) {
    val sleepRecords by viewModel.sleepRecords.collectAsState()

    var date by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var quality by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sleep Page", fontSize = 24.sp)

        TextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date (YYYY-MM-DD)") })
        TextField(
            value = startTime,
            onValueChange = { startTime = it },
            label = { Text("Start Time (HH:MM)") })
        TextField(
            value = endTime,
            onValueChange = { endTime = it },
            label = { Text("End Time (HH:MM)") })
        TextField(
            value = quality.toString(),
            onValueChange = { quality = it.toIntOrNull() ?: 1 },
            label = { Text("Quality (1-5)") })

        Button(onClick = {
            val sleep = Sleep(
                date = date,
                startTime = startTime,
                endTime = endTime,
                quality = quality
            )
            viewModel.addSleepRecord(sleep)
        }) {
            Text("Add Sleep Record")
        }

        LazyColumn {
            items(sleepRecords) { sleep ->
                Text(
                    text = "Date: ${sleep.date}, Start: ${sleep.startTime}, End: ${sleep.endTime}, Quality: ${sleep.quality}"
                )
            }
        }
    }
}