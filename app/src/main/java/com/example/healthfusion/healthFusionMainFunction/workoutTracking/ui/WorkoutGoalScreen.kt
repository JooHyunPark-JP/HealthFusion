package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.healthfusion.ui.theme.HealthFusionTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutGoalScreen() {
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
                        containerColor = MaterialTheme.colorScheme.inversePrimary // 배경색 설정
                    )
                )
            }
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // 왼쪽 세션: Daily Workout Goal
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 56.dp), // FAB 공간 확보를 위해 패딩 추가
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(text = "Daily Goal", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                        // ProgressBar for Daily Workout Goal
                        val dailyProgress = 0.5f // Progress value between 0.0f and 1.0f
                        LinearProgressIndicator(
                            progress = { dailyProgress },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Text(text = "${(dailyProgress * 100).toInt()}% completed")

                        // LazyColumn for daily goals
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(5) { index -> // 5개의 목표를 예제로 추가
                                Text(text = "Daily Goal Item $index")
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                    // FloatingActionButton for Daily Goals
                    FloatingActionButton(
                        onClick = { /* Add daily goal logic here */ },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Daily Goal")
                    }
                }

                // Vertical Divider
                VerticalDivider(
                    color = Color.Gray,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                )

                // 오른쪽 세션: Weekly Workout Goal
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 56.dp), // FAB 공간 확보를 위해 패딩 추가
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(text = "Weekly Goal", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                        // ProgressBar for Weekly Workout Goal
                        val weeklyProgress = 0.8f // Progress value between 0.0f and 1.0f
                        LinearProgressIndicator(
                            progress = { weeklyProgress },
                            modifier = Modifier.fillMaxWidth(),
                        )

                        Text(text = "${(weeklyProgress * 100).toInt()}% completed")

                        // LazyColumn for weekly goals
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(5) { index -> // 5개의 목표를 예제로 추가
                                Text(text = "Weekly Goal Item $index")
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                    // FloatingActionButton for Weekly Goals
                    FloatingActionButton(
                        onClick = { /* Add weekly goal logic here */ },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Weekly Goal")
                    }
                }
            }
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