package com.example.healthfusion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.healthfusion.healthFusionMainFunction.dietTracking.ui.DietViewModel
import com.example.healthfusion.healthFusionMainFunction.login.ui.LoginScreen
import com.example.healthfusion.healthFusionMainFunction.login.ui.LoginViewModel
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.ui.SleepViewModel
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.WorkoutViewModel
import com.example.healthfusion.healthFusionNav.BottomNavBar
import com.example.healthfusion.healthFusionNav.NavGraph
import com.example.healthfusion.ui.theme.HealthFusionTheme
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val workoutViewModel: WorkoutViewModel by viewModels()
    private val dietViewModel: DietViewModel by viewModels()
    private val sleepViewModel: SleepViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HealthFusionTheme {
                val navController = rememberNavController()

                //firebase login
                val currentUser =
                    remember { mutableStateOf(firebaseAuth.currentUser) }

                firebaseAuth.addAuthStateListener { auth ->
                    currentUser.value = auth.currentUser
                }

                //Checks user is whether signed out or not
                val authState by produceState(initialValue = currentUser.value) {
                    val authListener = FirebaseAuth.AuthStateListener { auth ->
                        value = auth.currentUser
                    }
                    firebaseAuth.addAuthStateListener(authListener)
                    awaitDispose {
                        firebaseAuth.removeAuthStateListener(authListener)
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (authState != null) {
                            BottomNavBar(navController = navController)
                        }
                    }) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        if (currentUser.value != null) {
                            NavGraph(
                                navController = navController,
                                workoutViewModel = workoutViewModel,
                                dietViewModel = dietViewModel,
                                sleepViewModel = sleepViewModel
                            )
                        } else {
                            LoginScreen(viewModel = loginViewModel)
                        }
                    }
                }
            }
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
