package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoal
import com.example.healthfusion.ui.theme.HealthFusionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutGoalScreen() {

    var dailyGoals by remember { mutableStateOf(listOf<WorkoutGoal>()) }
    var weeklyGoals by remember { mutableStateOf(listOf<WorkoutGoal>()) }

    HealthFusionTheme {
        Scaffold(
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
            topBar = {
                TopAppBar(
                    title = { Text("Workout Goals") },
                    navigationIcon = {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "ArrowBack")
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.inversePrimary
                    )
                )
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                WorkoutGoalSection(
                    title = "Daily Goal",
                    progress = 0.5f,
                    goals = dailyGoals,
                    onAddGoalClick = { newDailyGoalText ->
                        dailyGoals = dailyGoals + WorkoutGoal(text = newDailyGoalText)
                    },
                    onGoalClick = { workoutGoal ->
                        dailyGoals = dailyGoals.map { goal ->
                            if (goal == workoutGoal) goal.copy(isCompleted = !goal.isCompleted) else goal
                        }
                    },
                    modifier = Modifier.weight(1f)
                )

                // Vertical Divider
                VerticalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )

                WorkoutGoalSection(
                    title = "Weekly Goal",
                    progress = 0.8f,
                    goals = weeklyGoals,
                    onAddGoalClick = { newWeeklyGoal ->
                        weeklyGoals = weeklyGoals + WorkoutGoal(text = newWeeklyGoal)
                    },
                    onGoalClick = { workoutGoal ->
                        weeklyGoals = weeklyGoals.map { goal ->
                            if (goal == workoutGoal) goal.copy(isCompleted = !goal.isCompleted) else goal
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun WorkoutGoalSection(
    title: String,
    progress: Float,
    goals: List<WorkoutGoal>,
    onAddGoalClick: (String) -> Unit,
    onGoalClick: (WorkoutGoal) -> Unit,
    modifier: Modifier = Modifier
) {

    var showDialog by remember { mutableStateOf(false) }

    val calculatedProgress = if (goals.isEmpty()) 0f else goals.size / 5f

    Box(
        modifier = modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth(),
            )

            Text(text = "${(calculatedProgress * 100).toInt()}% completed")

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(goals.size) { index ->
                    val goal = goals[index]
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(if (goal.isCompleted) Color.Gray else Color.Transparent)
                            .clickable { onGoalClick(goal) } // 클릭 시 goal 상태 업데이트
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = goal.text)
                        if (goal.isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = "Completed")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Goal")
        }

        if (showDialog) {
            AddGoalDialog(
                onDismiss = { showDialog = false },
                onConfirm = { newGoal ->
                    onAddGoalClick(newGoal)
                })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WorkoutGoalScreenPreview() {
    HealthFusionTheme {
        WorkoutGoalScreen()
    }
}