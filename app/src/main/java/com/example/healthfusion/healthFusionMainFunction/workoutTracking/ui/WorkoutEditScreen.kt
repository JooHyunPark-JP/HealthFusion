package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.NumberPicker
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.healthfusion.R
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AnaerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.FieldInfo
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.FieldType
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.example.healthfusion.ui.theme.HealthFusionTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun WorkoutEdit(viewModel: WorkoutViewModel, workoutName: String, workoutType: WorkoutType) {

    val context = LocalContext.current

    // Retrieve the corresponding enum based on the workoutType
    val workoutEnum: Any? = when (workoutType) {
        WorkoutType.AEROBIC -> AerobicWorkout.entries.find { it.workoutName == workoutName }
        WorkoutType.ANAEROBIC -> AnaerobicWorkout.entries.find { it.workoutName == workoutName }
    }

    if (workoutEnum == null) {
        Text("Invalid Workout Type or Name")
        return
    }

    // Get the fields from the workout enum
    val fields = when (workoutEnum) {
        is AerobicWorkout -> workoutEnum.fields
        is AnaerobicWorkout -> workoutEnum.fields
        else -> emptyList()
    }

    // Display name for the workout
    val displayName = when (workoutEnum) {
        is AerobicWorkout -> workoutEnum.workoutName
        is AnaerobicWorkout -> workoutEnum.workoutName
        else -> "Unknown Workout"
    }

    // Scroll state
    val scrollState = rememberScrollState()

    // MutableStateMap to track input values
    val inputValues = remember { mutableStateMapOf<FieldInfo, String>() }

    // State for the selected workout date
    val currentTimestamp = System.currentTimeMillis()
    var selectedDate by remember { mutableLongStateOf(currentTimestamp) }
    var showDatePicker by remember { mutableStateOf(false) }

    // UI rendering
    HealthFusionTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = displayName.replace("_", " "),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF5F4)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp)
            ) {
                DynamicWorkoutInputFields(
                    fields = fields.map { fieldInfo ->
                        WorkoutField(
                            label = fieldInfo.label,
                            value = inputValues.getOrDefault(fieldInfo, ""),
                            type = fieldInfo.type,
                            onChange = { inputValues[fieldInfo] = it }
                        )
                    },
                    inputValues = inputValues
                )
            }

            // Date Picker Button

            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier.padding(horizontal = 32.dp),

                ) {

                Icon(
                    painterResource(id = R.drawable.ic_calendar),
                    contentDescription = "calendar",
                    tint = Color.White
                )
                Text(" Select Date: ${formatDate(selectedDate)}")
            }

            // DatePickerDialog
            if (showDatePicker) {
                val datePickerDialog = DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, dayOfMonth)
                        selectedDate = calendar.timeInMillis
                        showDatePicker = false
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                )

                datePickerDialog.setOnDismissListener {
                    showDatePicker = false
                }

                datePickerDialog.show()
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    viewModel.addWorkout(
                        name = displayName,
                        type = workoutType,
                        duration = inputValues[FieldInfo.DURATION]?.toIntOrNull(),
                        distance = inputValues[FieldInfo.DISTANCE]?.toIntOrNull(),
                        caloriesBurned = inputValues[FieldInfo.CALORIES_BURNED]?.toIntOrNull(),
                        set = inputValues[FieldInfo.SETS]?.toIntOrNull(),
                        repetition = inputValues[FieldInfo.REPETITIONS]?.toIntOrNull(),
                        weight = inputValues[FieldInfo.WEIGHTS]?.toIntOrNull(),
                        workoutDate = selectedDate
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


fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@SuppressLint("DefaultLocale")
@Composable
fun DynamicWorkoutInputFields(
    fields: List<WorkoutField>,
    inputValues: MutableMap<FieldInfo, String>
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        fields.forEach { field ->
            when (field.type) {

                FieldType.TIMEPICKER -> {
                    TimePickerWithSpinners(inputValues = inputValues)
                }

                FieldType.TEXT -> {
                    TextField(
                        value = field.value,
                        onValueChange = field.onChange, // Update inputValues when changed
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

                FieldType.TIMER -> {
                    TimerComponentWithToggle(
                        field = field,
                        inputValues = inputValues
                    )

                }
            }
        }
    }
}

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
        Text(
            text = "Use a timer if needed (click)",
            style = TextStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
                fontSize = 16.sp
            ),
            modifier = Modifier
                .clickable { showTimer = !showTimer }
                .padding(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Conditionally render the TimerComponent
        if (showTimer) {
            TimerComponent(inputValues = inputValues)
        }
    }
}


// Convert seconds into a formatted string (HH:mm:ss)
@SuppressLint("DefaultLocale")
fun formatSeconds(seconds: Long): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return String.format("%02d:%02d:%02d", h, m, s)
}

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


data class WorkoutField(
    val label: String,
    val value: String,
    val type: FieldType,
    val onChange: (String) -> Unit
)

