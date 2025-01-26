package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthfusion.R
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AnaerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.FieldInfo
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.FieldType
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.editScreenUI.SegmentedControl
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.editScreenUI.TimePickerWithSpinners
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.editScreenUI.TimerComponentWithToggle
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
                        workoutDate = selectedDate,
                        equipmentType = inputValues[FieldInfo.EQUIPMENT_TYPE],
                        gripStyle = inputValues[FieldInfo.GRIP_STYLE]
                    )
                    Toast.makeText(
                        context,
                        "New $displayName data has been created!",
                        Toast.LENGTH_LONG
                    ).show()
                    viewModel.updateCurrentProgress(displayName)
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

                FieldType.TIMER -> {
                    TimerComponentWithToggle(
                        field = field,
                        inputValues = inputValues
                    )

                }

                FieldType.SEGMENTED -> {
                    when (field.label) {
                        "Equipment Type" -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = "Equipment Type:",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(6.dp)
                                )
                                SegmentedControl(
                                    options = listOf("Dumbbell", "Barbell", "No Equipment"),
                                    selectedOption = inputValues.getOrDefault(
                                        FieldInfo.EQUIPMENT_TYPE,
                                        "Dumbbell"
                                    ),
                                    onOptionSelected = {
                                        inputValues[FieldInfo.EQUIPMENT_TYPE] = it
                                    }
                                )
                            }
                        }

                        "Grip Style" -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = "Grip Type:",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .padding(6.dp)
                                )
                                SegmentedControl(
                                    options = listOf("Neutral", "Overhand", "Underhand"),
                                    selectedOption = inputValues.getOrDefault(
                                        FieldInfo.GRIP_STYLE,
                                        "Neutral"
                                    ),
                                    onOptionSelected = { inputValues[FieldInfo.GRIP_STYLE] = it }
                                )
                            }
                        }
                    }
                }
            }
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


data class WorkoutField(
    val label: String,
    val value: String,
    val type: FieldType,
    val onChange: (String) -> Unit
)

