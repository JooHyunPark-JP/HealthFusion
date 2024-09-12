package com.example.healthfusion.healthFusionMainFunction.dietTracking.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun DietScreen(viewModel: DietViewModel, modifier: Modifier = Modifier) {
    val diets by viewModel.diets.collectAsState()

    var name by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Food Name") })
        TextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Calories") })

        Button(onClick = {

            viewModel.addDiet(name = name, calories = calories.toInt())
        }) {
            Text("Add Food")
        }

        LazyColumn {
            items(diets) { diet ->
                Text(
                    text = "Food: ${diet.name}, Calories: ${diet.calories}"
                )
            }
        }
    }
}