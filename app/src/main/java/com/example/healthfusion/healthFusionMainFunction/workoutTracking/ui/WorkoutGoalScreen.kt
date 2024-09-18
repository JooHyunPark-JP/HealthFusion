package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoal
import com.example.healthfusion.ui.theme.HealthFusionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutGoalScreen(viewModel: WorkoutViewModel) {

    val dailyGoals by viewModel.dailyGoals.collectAsState()
    val weeklyGoals by viewModel.weeklyGoals.collectAsState()


    val tabWorkoutPageTitles = listOf("Daily Goal", "Weekly Goal")

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    HealthFusionTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {


            TabRow(selectedTabIndex = selectedTabIndex) {
                tabWorkoutPageTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            if (selectedTabIndex == 0) {
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
                    onGoalDelete = { workoutGoal ->
                        viewModel.deleteWorkoutGoal(workoutGoal)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            if (selectedTabIndex == 1) {
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
                    onGoalDelete = { workoutGoal ->
                        viewModel.deleteWorkoutGoal(workoutGoal)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
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
    onGoalDelete: (WorkoutGoal) -> Unit,
    modifier: Modifier = Modifier
) {

    var showDialog by rememberSaveable { mutableStateOf(false) }
    val completedGoals = goals.count { it.isCompleted }
    val totalGoals = goals.size


    Box(
        modifier = modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp)
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(goals.size) { index ->
                    val goal = goals[index]
                    GoalItem(
                        goal = goal,
                        onGoalClick = onGoalClick,
                        onGoalDelete = onGoalDelete
                    )
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
    onGoalClick: (WorkoutGoal) -> Unit,
    onGoalDelete: (WorkoutGoal) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .background(if (goal.isCompleted) Color.Gray else Color.Transparent)
            .clickable { onGoalClick(goal) }
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        if (goal.isCompleted) {
            Icon(
                Icons.Default.Check,
                contentDescription = "Completed",
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        Text(
            text = goal.text,
            modifier = Modifier.weight(1f),
        )

        IconButton(onClick = { onGoalDelete(goal) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Goal")
        }

        Spacer(modifier = Modifier.height(4.dp))
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
