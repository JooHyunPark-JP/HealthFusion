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
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.navigation.NavController
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoal
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoalType
import com.example.healthfusion.healthFusionNav.Screen
import com.example.healthfusion.ui.theme.HealthFusionTheme


@Composable
fun WorkoutGoalScreen(viewModel: WorkoutViewModel, navController: NavController) {

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
                    goalType = WorkoutGoalType.DAILY,
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
                    navController = navController,
                    onGoalCompleted = { goal ->
                        viewModel.markGoalAsCompleted(goal)
                    },
                    onGoalNotCompletedYet = { goal ->
                        viewModel.markGoalAsNotCompleted(goal)
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
                    goalType = WorkoutGoalType.WEEKLY,
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
                    navController = navController,

                    onGoalCompleted = { goal ->
                        viewModel.markGoalAsCompleted(goal)
                    },
                    onGoalNotCompletedYet = { goal ->
                        viewModel.markGoalAsNotCompleted(goal)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutGoalSection(
    title: String,
    goals: List<WorkoutGoal>,
    goalType: WorkoutGoalType,
    onAddGoalClick: (String) -> Unit,
    onGoalClick: (WorkoutGoal) -> Unit,
    onGoalDelete: (WorkoutGoal) -> Unit,
    onGoalCompleted: (WorkoutGoal) -> Unit,
    onGoalNotCompletedYet: (WorkoutGoal) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {

    var showDialog by rememberSaveable { mutableStateOf(false) }
    val completedGoals = goals.count { it.isCompleted }
    val totalGoals = goals.size

    var showBottomSheet by remember { mutableStateOf(false) }


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
                        onGoalDelete = onGoalDelete,
                        onGoalCompleted = onGoalCompleted,
                        onGoalNotCompletedYet = onGoalNotCompletedYet
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { showBottomSheet = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Goal")
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Choose an option:", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = {
                        // Custom goal logic
                        showBottomSheet = false
                        showDialog = true
                    }) {
                        Text("Write a custom goal")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = {
                        navController.navigate(Screen.WorkoutGoalList.createRoute(goalType))
                        showBottomSheet = false
                    }) {
                        Text("Choose workout and frequency")
                    }
                }
            }
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
    onGoalDelete: (WorkoutGoal) -> Unit,
    onGoalCompleted: (WorkoutGoal) -> Unit,
    onGoalNotCompletedYet: (WorkoutGoal) -> Unit
) {
    val isAutoManaged = goal.workoutName.isNotEmpty() && goal.goalFrequency > 0
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
            .background(if (goal.isCompleted) Color.Gray else Color.Transparent)
            .then(
                if (!isAutoManaged) Modifier.clickable { onGoalClick(goal) }
                else Modifier
            )
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

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Text(
                text = goal.text,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            if (isAutoManaged) {
                Text(
                    text = "Progress: ${goal.currentProgress}/${goal.goalFrequency}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                if (goal.goalFrequency <= goal.currentProgress && !goal.isCompleted) {
                    onGoalCompleted(goal)
                } else if (goal.goalFrequency > goal.currentProgress && goal.isCompleted) {
                    onGoalNotCompletedYet(goal)
                }

            }

        }

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
