package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.AnaerobicWorkout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.example.healthfusion.ui.theme.HealthFusionTheme

@Composable
fun WorkoutGoalList(workoutViewModel: WorkoutViewModel) {
    val workouts by workoutViewModel.workouts.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }

    val tabTitles = listOf(WorkoutType.AEROBIC, WorkoutType.ANAEROBIC)


    HealthFusionTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            TabRow(selectedTabIndex = selectedTabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title.toString()) }
                    )
                }
            }
            val selectedWorkouts = if (selectedTabIndex == 0) {
                AerobicWorkout.entries
            } else {
                AnaerobicWorkout.entries
            }


            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(selectedWorkouts) { _, workout ->
                    WorkoutGridItem(
                        workout = workout,
                        onClick = {
                            //TO do
                        }
                    )

                }
            }
        }

    }
}