package com.example.healthfusion.healthFusionNav

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.healthfusion.R

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        Screen.Workout,
        Screen.Diet,
        Screen.Sleep
    )
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            NavigationBarItem(
                icon = {
                    when (screen) {
                        is Screen.Workout -> Icon(
                            painterResource(id = R.drawable.ic_placeholder_icon),
                            contentDescription = null
                        )

                        is Screen.Diet -> Icon(
                            painterResource(id = R.drawable.ic_placeholder_icon),
                            contentDescription = null
                        )

                        is Screen.Sleep -> Icon(
                            painterResource(id = R.drawable.ic_placeholder_icon),
                            contentDescription = null
                        )
                    }
                },
                label = { Text(text = screen.route.replaceFirstChar { it.uppercase() }) },
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route)
                    }
                }
            )
        }
    }
}