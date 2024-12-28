package com.example.healthfusion.healthFusionMainFunction.dietTracking.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
    var searchQuery by remember { mutableStateOf("") }
  //  val searchResults by viewModel.searchResults.collectAsState()
    val diets by viewModel.diets.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search Food") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = { /*viewModel.searchFood(searchQuery)*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 검색 결과 리스트
        Text("Search Results", style = MaterialTheme.typography.bodyMedium)
/*        LazyColumn {
            items(searchResults) { diet ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Name: ${diet.name}")
                        Text("Calories: ${diet.calories} kcal")
                        Button(onClick = { viewModel.addDiet(diet.name, diet.calories) }) {
                            Text("Add")
                        }
                    }
                }
            }
        }*/

        Spacer(modifier = Modifier.height(16.dp))

        Text("Added Diets", style = MaterialTheme.typography.bodyMedium)
        LazyColumn {
            items(diets) { diet ->
                Text("${diet.name} - ${diet.calories} kcal")
            }
        }
    }
}