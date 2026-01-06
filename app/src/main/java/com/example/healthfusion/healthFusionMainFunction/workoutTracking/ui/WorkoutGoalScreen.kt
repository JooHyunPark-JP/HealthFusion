package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
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
    var showBottomSheet by remember { mutableStateOf(false) }

    val completedGoals = goals.count { it.isCompleted }
    val totalGoals = goals.size
    val calculatedProgress =
        if (totalGoals == 0) 0f else completedGoals / totalGoals.toFloat()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium
                    )

                    LinearProgressIndicator(
                        progress = { calculatedProgress },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = if (totalGoals > 0) {
                            "$completedGoals of $totalGoals completed"
                        } else {
                            "You don't have any $title"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (goals.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Let's create $title by clicking + button below!",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Ex: Walk 2 times, Do 20 push ups",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
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
                    Text(
                        "Choose an option:",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = {
                        showBottomSheet = false
                        showDialog = true
                    }) {
                        Text("Write a custom goal")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(onClick = {
                        navController.navigate(
                            Screen.WorkoutGoalList.createRoute(goalType)
                        )
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
                }
            )
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

    if (isAutoManaged) {
        if (goal.goalFrequency <= goal.currentProgress && !goal.isCompleted) {
            onGoalCompleted(goal)
        } else if (goal.goalFrequency > goal.currentProgress && goal.isCompleted) {
            onGoalNotCompletedYet(goal)
        }
    }

    val targetColor = if (goal.isCompleted) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    val containerColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 250),
        label = "goalColorAnim"
    )

    val scale by animateFloatAsState(
        targetValue = if (goal.isCompleted) 1.03f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "goalScaleAnim"
    )

    val elevation by animateDpAsState(
        targetValue = if (goal.isCompleted) 6.dp else 1.dp,
        animationSpec = tween(durationMillis = 200),
        label = "goalElevationAnim"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .then(
                if (!isAutoManaged) {
                    Modifier.clickable { onGoalClick(goal) }
                } else {
                    Modifier
                }
            ),
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = goal.text,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    textDecoration = if (goal.isCompleted) {
                        TextDecoration.LineThrough
                    } else {
                        null
                    }
                )

                if (isAutoManaged) {
                    Text(
                        text = "Progress: ${goal.currentProgress}/${goal.goalFrequency}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AnimatedVisibility(visible = goal.isCompleted) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Completed",
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Done",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                IconButton(onClick = { onGoalDelete(goal) }) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete Goal"
                    )
                }
            }
        }
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
