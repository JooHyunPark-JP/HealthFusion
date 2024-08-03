package com.example.healthfusion.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.healthfusion.data.Exercise
import com.example.healthfusion.data.ExerciseType
import com.example.healthfusion.ui.theme.HealthFusionTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val exerciseViewModel: ExerciseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HealthFusionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        exerciseViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, exerciseViewModel: ExerciseViewModel, modifier: Modifier = Modifier) {
    val exercises by exerciseViewModel.exercises.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            val exercise = Exercise(
                name = "drinking",
                duration = 300,
                caloriesBurned = 400,
                type = ExerciseType.AEROBIC
            )
            exerciseViewModel.addExercise(exercise)
        }
    }

    // Display the exercises
    LazyColumn(modifier = modifier) {
        items(items = exercises) { exercise ->
            Text(
                text = "Exercise: ${exercise.name}, Duration: ${exercise.duration}, Calories Burned: ${exercise.caloriesBurned}, Type: ${exercise.type}"
            )
        }
        item {
            Text(
                text = "Hello $name!",
                modifier = modifier
            )
        }
    }

}
/*

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HealthFusionTheme {
        Greeting("Android")
    }
}*/
