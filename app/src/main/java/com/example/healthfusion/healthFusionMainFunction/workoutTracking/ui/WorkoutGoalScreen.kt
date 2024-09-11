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
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoal
import com.example.healthfusion.ui.theme.HealthFusionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutGoalScreen(navController: NavHostController, viewModel: WorkoutViewModel) {

    val dailyGoals by viewModel.dailyGoals.collectAsState()
    val weeklyGoals by viewModel.weeklyGoals.collectAsState()

    HealthFusionTheme {
        Scaffold(
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing),
            topBar = {
                TopAppBar(
                    title = { Text("Workout Goals") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
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
                    goals = dailyGoals,
                    onAddGoalClick = { newDailyGoalText ->
                        viewModel.addWorkoutGoal(newDailyGoalText)
                    },
                    onGoalClick = { workoutGoal ->
                        val updatedGoal = workoutGoal.copy(isCompleted = !workoutGoal.isCompleted)
                        viewModel.updateWorkoutGoal(updatedGoal)
                    },
                    modifier = Modifier.weight(1f)
                )

                VerticalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )

                WorkoutGoalSection(
                    title = "Weekly Goal",
                    goals = weeklyGoals,
                    onAddGoalClick = { newWeeklyGoalText ->
                        viewModel.addWeeklyGoal(newWeeklyGoalText)
                    },
                    onGoalClick = { workoutGoal ->
                        val updatedGoal = workoutGoal.copy(isCompleted = !workoutGoal.isCompleted)
                        viewModel.updateWorkoutGoal(updatedGoal)
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
    goals: List<WorkoutGoal>,
    onAddGoalClick: (String) -> Unit,
    onGoalClick: (WorkoutGoal) -> Unit,
    modifier: Modifier = Modifier
) {

    var showDialog by rememberSaveable { mutableStateOf(false) }

    val completedGoals = goals.count { it.isCompleted }
    val totalGoals = goals.size


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

            val calculatedProgress =
                if (totalGoals == 0) 0f else completedGoals / totalGoals.toFloat()

            LinearProgressIndicator(
                progress = { calculatedProgress },
                modifier = Modifier.fillMaxWidth(),
            )

            Text(text = "$completedGoals of $totalGoals $title completed")

            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(goals.size) { index ->
                    val goal = goals[index]
                    GoalItem(goal = goal, onGoalClick = onGoalClick)
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

@Composable
fun GoalItem(
    goal: WorkoutGoal,
    onGoalClick: (WorkoutGoal) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .background(if (goal.isCompleted) Color.Gray else Color.Transparent)
            .clickable { onGoalClick(goal) }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = goal.text,
            modifier = Modifier.weight(1f),
        )
        if (goal.isCompleted) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Completed",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}

/*
@Preview(showBackground = true)
@Composable
fun WorkoutGoalScreenPreview() {
    HealthFusionTheme {
        val fakeNavController = rememberNavController()
        WorkoutGoalScreen(navController = fakeNavController)
    }
}*/
