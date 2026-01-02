package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AnaerobicStrengthMetric
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.FieldInfo
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.Workout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.getFieldValue
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.valueFor
import com.example.healthfusion.util.DateFormatter
import ir.ehsannarmani.compose_charts.LineChart
import ir.ehsannarmani.compose_charts.models.DotProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.Line
import ir.ehsannarmani.compose_charts.models.PopupProperties
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class WorkoutRange {
    ONE_MONTH,
    TWO_MONTH,
    THREE_MONTHS,
    SIX_MONTHS,
    ALL
}

@Composable
fun WorkoutHistoryScreen(
    viewModel: WorkoutViewModel,
    dateFormatter: DateFormatter,
    modifier: Modifier = Modifier
) {
    val workouts by viewModel.workouts.collectAsState()

    var selectedWorkoutNameFilter by remember { mutableStateOf<String?>(null) }

    val workoutTypeChips = remember(workouts) {
        workouts
            .groupBy { it.name }
            .mapNotNull { (name, list) ->
                val lastDate = list.maxOfOrNull { it.workoutDate }
                // name: String, lastDate: Long
                lastDate?.let { name to it }
            }
            .sortedByDescending { (_, lastDate) -> lastDate }
    }

    var selectedRange by remember { mutableStateOf(WorkoutRange.ALL) }

    var selectedAerobicWorkout by remember { mutableStateOf<Workout?>(null) }
    var selectedAnaerobicWorkout by remember { mutableStateOf<Workout?>(null) }
    var expandedAerobic by remember { mutableStateOf(false) }
    var expandedAnaerobic by remember { mutableStateOf(false) }
    var selectedDetailOption by remember { mutableStateOf<String?>(null) }

    var selectedAnaerobicMetric by remember {
        mutableStateOf<AnaerobicStrengthMetric?>(null)
    }

    var workoutTabIndex by remember { mutableIntStateOf(0) }
    val tabWorkoutPageTitles = listOf("Line Chart", "Workout List")

    var selectedUnit by remember { mutableStateOf("minutes") }
    var expandedUnitSelection by remember { mutableStateOf(false) }


    Column(modifier = Modifier.padding(16.dp)) {

        val now = System.currentTimeMillis()
        val oneMonthMillis = 30L * 24 * 60 * 60 * 1000
        val twoMonthMillis = 60L * 24 * 60 * 60 * 1000
        val threeMonthMillis = 90L * 24 * 60 * 60 * 1000
        val sixMonthMillis = 180L * 24 * 60 * 60 * 1000

        val filteredWorkouts = workouts.filter { workout ->

            val inRange = when (selectedRange) {
                WorkoutRange.ALL -> true
                WorkoutRange.ONE_MONTH ->
                    workout.workoutDate >= now - oneMonthMillis

                WorkoutRange.TWO_MONTH ->
                    workout.workoutDate >= now - twoMonthMillis

                WorkoutRange.THREE_MONTHS ->
                    workout.workoutDate >= now - threeMonthMillis

                WorkoutRange.SIX_MONTHS ->
                    workout.workoutDate >= now - sixMonthMillis
            }

            inRange &&
                    (selectedAerobicWorkout == null || workout.name == selectedAerobicWorkout?.name) &&
                    (selectedAnaerobicWorkout == null || workout.name == selectedAnaerobicWorkout?.name) &&
                    (selectedWorkoutNameFilter == null || workout.name == selectedWorkoutNameFilter)
        }

        if (workoutTypeChips.isNotEmpty()) {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedWorkoutNameFilter == null,
                        onClick = {
                            selectedWorkoutNameFilter = null
                            selectedAerobicWorkout = null
                            selectedDetailOption = null
                            selectedAnaerobicWorkout = null
                            selectedAnaerobicMetric = null
                        },
                        label = { Text("All") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                items(workoutTypeChips) { (name, _) ->
                    val isSelected = selectedWorkoutNameFilter == name
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            val newFilter = if (isSelected) null else name
                            selectedWorkoutNameFilter = newFilter

                            if (newFilter == null) {
                                selectedAerobicWorkout = null
                                selectedAnaerobicWorkout = null
                                selectedDetailOption = null
                                selectedAnaerobicMetric = null
                            } else {
                                val selectedWorkout = workouts.firstOrNull { it.name == newFilter }
                                when (selectedWorkout?.type) {
                                    WorkoutType.AEROBIC -> {
                                        selectedAerobicWorkout = selectedWorkout
                                        selectedAnaerobicWorkout = null
                                        selectedDetailOption = null
                                        selectedAnaerobicMetric = null
                                    }

                                    WorkoutType.ANAEROBIC -> {
                                        selectedAnaerobicWorkout = selectedWorkout
                                        selectedAerobicWorkout = null
                                        selectedAnaerobicMetric = null
                                        selectedDetailOption = null
                                    }

                                    else -> {
                                        selectedAerobicWorkout = null
                                        selectedAnaerobicWorkout = null
                                        selectedDetailOption = null
                                        selectedAnaerobicMetric = null
                                    }
                                }
                            }
                        },
                        label = { Text(name.replace("_", " ")) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }

        if (selectedAerobicWorkout != null) {
            val workoutEnum =
                AerobicWorkout.entries.find { it.workoutName == selectedAerobicWorkout?.name }

            val validFieldsForDropdown = workoutEnum?.fields?.filter { fieldInfo ->
                fieldInfo !in listOf(FieldInfo.SETS, FieldInfo.REPETITIONS)
            } ?: emptyList()

            LaunchedEffect(selectedAerobicWorkout) {
                if (validFieldsForDropdown.isNotEmpty()) {
                    val stillValid = validFieldsForDropdown.any { it.label == selectedDetailOption }
                    if (!stillValid) {
                        selectedDetailOption = validFieldsForDropdown.first().label
                    }
                } else {
                    selectedDetailOption = null
                }
            }

            if (validFieldsForDropdown.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 1.dp)
                ) {
                    items(validFieldsForDropdown) { fieldInfo ->
                        val isSelected = selectedDetailOption == fieldInfo.label

                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedDetailOption = fieldInfo.label
                            },
                            label = { Text(fieldInfo.label) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        }

        //  Anaerobic metric chips (Volume / Top Weight / Reps / Sets)
        if (selectedAnaerobicWorkout != null) {
            val strengthMetrics = AnaerobicStrengthMetric.entries.toList()

            LaunchedEffect(selectedAnaerobicWorkout) {
                if (strengthMetrics.isNotEmpty()) {
                    if (selectedAnaerobicMetric == null ||
                        selectedAnaerobicMetric !in strengthMetrics
                    ) {
                        selectedAnaerobicMetric = strengthMetrics.first()
                    }
                } else {
                    selectedAnaerobicMetric = null
                }
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(strengthMetrics) { metric ->
                    val isSelected = selectedAnaerobicMetric == metric

                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedAnaerobicMetric = metric },
                        label = { Text(metric.shortLabel) } // "Volume", "Top Wt" 등
                    )
                }
            }
        }

        val rangeOptions = listOf(
            WorkoutRange.ONE_MONTH to "1M",
            WorkoutRange.TWO_MONTH to "2M",
            WorkoutRange.THREE_MONTHS to "3M",
            WorkoutRange.SIX_MONTHS to "6M",
            WorkoutRange.ALL to "All"
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 1.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(rangeOptions) { (range, label) ->
                FilterChip(
                    selected = selectedRange == range,
                    onClick = { selectedRange = range },
                    label = { Text(label) }
                )
            }
        }

        val isWorkoutSelected =
            selectedAerobicWorkout != null || selectedAnaerobicWorkout != null

        if (isWorkoutSelected) {
            TabRow(selectedTabIndex = workoutTabIndex) {
                tabWorkoutPageTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = workoutTabIndex == index,
                        onClick = { workoutTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }
        }

        when {
            // if no workout records at all
            workouts.isEmpty() -> {
                Text(
                    text = "No workout history yet.\nStart logging workouts to see your progress.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    textAlign = TextAlign.Center
                )
            }

            //if user choose Anaerobic workout, show the list

            selectedAnaerobicWorkout != null && filteredWorkouts.isNotEmpty() -> {

                when (workoutTabIndex) {
                    0 -> {
                        val metric = selectedAnaerobicMetric ?: AnaerobicStrengthMetric.TOTAL_VOLUME

                        val sortedByDate = filteredWorkouts.sortedBy { it.workoutDate }

                        val lineData: List<Double> = sortedByDate.map { workout ->
                            metric.valueFor(workout)
                        }

                        AnaerobicLineChart(
                            lineData = lineData,
                            metric = metric,
                            dateLabels = getDateLabels(sortedByDate),
                            filteredWorkouts = sortedByDate
                        )
                    }

                    1 -> {
                        val sortedByDescent = filteredWorkouts.sortedByDescending { it.workoutDate }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Your '${
                                    sortedByDescent.first().name.replace(
                                        "_",
                                        " "
                                    )
                                }' data"
                            )
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                items(sortedByDescent.size) { index ->
                                    val workout = sortedByDescent[index]
                                    WorkoutItem(
                                        workout = workout,
                                        onDeleteClick = { viewModel.deleteWorkout(workout) },
                                        dateFormatter = dateFormatter
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // if user choose Aerobic workout, show the line chart
            selectedAerobicWorkout != null && filteredWorkouts.size >= 2 -> {
                Spacer(modifier = Modifier.height(8.dp))
                when (workoutTabIndex) {
                    0 -> {
                        val lineData: List<Double> = filteredWorkouts.map { workout ->
                            val selectedField =
                                FieldInfo.entries.find { it.label == selectedDetailOption }

// Convert duration from seconds to minutes if the selected option is "Duration"
                            val rawValue =
                                selectedField?.let { workout.getFieldValue(it) } ?: 0.0
                            if (selectedDetailOption == "Duration") {
                                convertDuration(rawValue, selectedUnit)
                            } else {
                                rawValue
                            }
                        }

                        AerobicLineChart(
                            lineData = lineData,
                            selectedDetailOption = selectedDetailOption,
                            dateLabels = getDateLabels(filteredWorkouts),
                            filteredWorkouts = filteredWorkouts
                        )
                    }

                    1 -> {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Workout Name: ${
                                    filteredWorkouts.first().name.replace(
                                        "_",
                                        " "
                                    )
                                }"
                            )
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                items(filteredWorkouts.size) { index ->
                                    val sortedByDescent =
                                        filteredWorkouts.sortedByDescending { it.workoutDate }
                                    val workout = sortedByDescent[index]
                                    WorkoutItem(
                                        workout = workout,
                                        showWorkoutName = false,
                                        onDeleteClick = { viewModel.deleteWorkout(workout) },
                                        dateFormatter = dateFormatter
                                    )
                                }
                            }
                        }
                    }
                }
            }

            selectedAerobicWorkout != null && filteredWorkouts.size == 1 -> {
                Text(
                    text = "You need at least two workout data to see the graph.\nTry a different date range or create more workout data.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    textAlign = TextAlign.Center
                )
            }

            // workout is selected but no data within the range
            (selectedAerobicWorkout != null || selectedAnaerobicWorkout != null) &&
                    filteredWorkouts.isEmpty() -> {
                Text(
                    text = "No workouts in this period.\nTry a different date range.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    textAlign = TextAlign.Center
                )

            }

            // no selected workout type yet
            else -> {
                Text(
                    text = "Please choose Workout Type",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.fillMaxWidth()) {
            val aerobicWorkout =
                workouts.filter { it.type == WorkoutType.AEROBIC }.distinctBy { it.name }
            Box(
                modifier = Modifier.weight(1f)
            ) {
                OutlinedButton(
                    onClick = {
                        expandedAerobic = true
                        expandedAnaerobic = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = selectedAerobicWorkout?.name?.replace("_", " ") ?: "Select Aerobic")
                }
                DropdownMenu(
                    expanded = expandedAerobic,
                    onDismissRequest = { expandedAerobic = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    aerobicWorkout.forEach { workout ->
                        DropdownMenuItem(
                            text = { Text(workout.name.replace("_", " ")) },
                            onClick = {
                                selectedAerobicWorkout = workout
                                selectedAnaerobicWorkout = null
                                expandedAerobic = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            val anaerobicWorkout =
                workouts.filter { it.type == WorkoutType.ANAEROBIC }.distinctBy { it.name }
            Box(
                modifier = Modifier.weight(1f)
            ) {
                OutlinedButton(
                    onClick = {
                        expandedAnaerobic = true
                        expandedAerobic = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = selectedAnaerobicWorkout?.name?.replace("_", " ")
                            ?: "Select Anaerobic"
                    )
                }
                DropdownMenu(
                    expanded = expandedAnaerobic,
                    onDismissRequest = { expandedAnaerobic = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    anaerobicWorkout.forEach { workout ->
                        DropdownMenuItem(
                            text = { Text(workout.name.replace("_", " ")) },
                            onClick = {
                                selectedAnaerobicWorkout = workout
                                selectedAerobicWorkout = null
                                expandedAnaerobic = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))


        if (selectedDetailOption == "Duration")
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = { expandedUnitSelection = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Unit: ${selectedUnit.replaceFirstChar { it.uppercase() }}")
                }
                DropdownMenu(
                    expanded = expandedUnitSelection,
                    onDismissRequest = { expandedUnitSelection = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("minutes", "hours").forEach { unit ->
                        DropdownMenuItem(
                            text = { Text(unit.replaceFirstChar { it.uppercase() }) },
                            onClick = {
                                selectedUnit = unit
                                expandedUnitSelection = false
                            }
                        )
                    }
                }
            }
    }
}


@Composable
fun AerobicLineChart(
    lineData: List<Double>,
    selectedDetailOption: String?,
    dateLabels: List<String>,
    filteredWorkouts: List<Workout>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
    ) {
        LineChart(
            data = listOf(
                Line(
                    label = selectedDetailOption ?: "Calories Burned",
                    values = lineData,
                    color = SolidColor(Color(0xFF23af92)),
                    curvedEdges = true,
                    dotProperties = DotProperties(
                        enabled = false,
                        color = SolidColor(Color.White),
                        strokeWidth = 3.dp,
                        radius = 5.dp,
                        strokeColor = SolidColor(Color(0xFF23af92))
                    ),
                    popupProperties = PopupProperties(
                        enabled = true,
                        containerColor = Color(0xFF23af92),
                        textStyle = TextStyle.Default.copy(fontSize = 12.sp),
                        cornerRadius = 6.dp,
                        contentBuilder = { value ->
                            val tolerance = 0.01
                            val workout = filteredWorkouts.find {
                                when (selectedDetailOption) {
                                    "Calories Burned" -> kotlin.math.abs(
                                        (it.caloriesBurned?.toDouble()
                                            ?: 0.0) - value
                                    ) < tolerance

                                    "Duration" -> kotlin.math.abs(
                                        (it.duration?.toDouble()
                                            ?: 0.0) - value
                                    ) < tolerance

                                    "Distance" -> kotlin.math.abs(
                                        (it.distance?.toDouble()
                                            ?: 0.0) - value
                                    ) < tolerance

                                    else -> kotlin.math.abs(
                                        (it.caloriesBurned?.toDouble()
                                            ?: 0.0) - value
                                    ) < tolerance
                                }
                            }
                            workout?.let {
                                "Date: ${
                                    SimpleDateFormat(
                                        "yyyy-MM-dd",
                                        Locale.getDefault()
                                    ).format(Date(it.workoutDate))
                                }\nWorkout: ${it.name}\nCalories: ${it.caloriesBurned}\nDuration: ${it.duration} mins"
                            } ?: "No data"
                        }
                    )
                )
            ),
            modifier = Modifier.fillMaxWidth(),
            labelProperties = LabelProperties(
                enabled = true,
                textStyle = TextStyle.Default.copy(
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                ),
                padding = 12.dp,
                labels = dateLabels,
                rotationDegreeOnSizeConflict = -45f,
                forceRotation = false
            )
        )
    }
}

@Composable
fun AnaerobicLineChart(
    lineData: List<Double>,
    metric: AnaerobicStrengthMetric,
    dateLabels: List<String>,
    filteredWorkouts: List<Workout>
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
    ) {
        LineChart(
            data = listOf(
                Line(
                    label = metric.label,
                    values = lineData,
                    color = SolidColor(Color(0xFF23af92)),
                    curvedEdges = true,
                    dotProperties = DotProperties(
                        enabled = false,
                        color = SolidColor(Color.White),
                        strokeWidth = 3.dp,
                        radius = 5.dp,
                        strokeColor = SolidColor(Color(0xFF23af92))
                    ),
                    popupProperties = PopupProperties(
                        enabled = true,
                        containerColor = Color(0xFF23af92),
                        textStyle = TextStyle.Default.copy(fontSize = 12.sp),
                        cornerRadius = 6.dp,
                        contentBuilder = { value ->
                            val tolerance = 0.01

                            val workout = filteredWorkouts.find {
                                kotlin.math.abs(
                                    metric.valueFor(it) - value
                                ) < tolerance
                            }

                            workout?.let {
                                val date = SimpleDateFormat(
                                    "yyyy-MM-dd",
                                    Locale.getDefault()
                                ).format(Date(it.workoutDate))

                                val metricValue = metric.valueFor(it)

                                "$date\n" +
                                        "${metric.label}: ${"%.1f".format(metricValue)} ${metric.unitLabel}\n" +
                                        "Workout: ${it.name.replace("_", " ")}"
                            } ?: "${metric.label}: ${"%.1f".format(value)} ${metric.unitLabel}"
                        }
                    )
                )
            ),
            modifier = Modifier.fillMaxWidth(),
            labelProperties = LabelProperties(
                enabled = true,
                textStyle = TextStyle.Default.copy(
                    fontSize = 12.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                ),
                padding = 12.dp,
                labels = dateLabels,
                rotationDegreeOnSizeConflict = -45f,
                forceRotation = false
            )
        )
    }
}

fun getDateLabels(filteredWorkouts: List<Workout>): List<String> {
    /*    // All workout data shown in the line chart... use it for later
    return filteredWorkouts.sortedBy { it.workoutDate }
            .map { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it.workoutDate)) }*/

    val firstWorkoutDate = filteredWorkouts.minOfOrNull { it.workoutDate }
    val lastWorkoutDate = filteredWorkouts.maxOfOrNull { it.workoutDate }

    return listOfNotNull(
        firstWorkoutDate?.let {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
        },
        lastWorkoutDate?.let {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
        }
    )
}

fun convertDuration(durationInSeconds: Double, unit: String): Double {
    return when (unit) {
        "minutes" -> durationInSeconds / 60.0
        "hours" -> durationInSeconds / 3600.0
        else -> durationInSeconds
    }
}
