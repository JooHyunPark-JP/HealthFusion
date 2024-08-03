package com.example.healthfusion.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.example.healthfusion.data.Exercise
import com.example.healthfusion.data.ExerciseDao
import com.example.healthfusion.data.ExerciseType
import com.example.healthfusion.ui.theme.HealthFusionTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var exerciseDao: ExerciseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HealthFusionTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        //Exercise object injection testing.
        lifecycleScope.launch {
            // Data insert
            val exercise = Exercise(
                name = "Running",
                duration = 30,
                caloriesBurned = 200,
                type = ExerciseType.AEROBIC
            )
            exerciseDao.insert(exercise)

            // Data review
            val exercises = exerciseDao.getAllExercises()
            for (ex in exercises) {
                Log.d(
                    "MainActivity",
                    "Exercise: ${ex.name}, Duration: ${ex.duration}, Calories Burned: ${ex.caloriesBurned}, Type: ${ex.type}"
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HealthFusionTheme {
        Greeting("Android")
    }
}