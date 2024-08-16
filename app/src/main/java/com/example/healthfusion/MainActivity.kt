package com.example.healthfusion

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.healthfusion.healthFusionMainFunction.dietTracking.ui.DietViewModel
import com.example.healthfusion.healthFusionMainFunction.login.navigator.AuthNavGraph
import com.example.healthfusion.healthFusionMainFunction.login.ui.LoginViewModel
import com.example.healthfusion.healthFusionMainFunction.login.ui.SignUpViewModel
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
    private val signUpViewModel: SignUpViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HealthFusionTheme {
                val navController = rememberNavController()
                val currentUserUid = loginViewModel.currentUserUid.collectAsState().value

                // Current Firebase user as state
                val currentUser =
                    remember { mutableStateOf(firebaseAuth.currentUser) }

                // Auth state listener to update currentUser state
                DisposableEffect(Unit) {
                    val authListener = FirebaseAuth.AuthStateListener { auth ->
                        currentUser.value = auth.currentUser
                    }
                    firebaseAuth.addAuthStateListener(authListener)
                    onDispose {
                        firebaseAuth.removeAuthStateListener(authListener)
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentUser.value != null) {
                            BottomNavBar(navController = navController)
                        }
                    }) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        //If user is already logged in
                        if (currentUser.value != null) {
                            // If user is signed in, get the UID
                            val userUid = currentUser.value?.uid

                            // checking user UID for debugging
                            Log.d("heybro1", "User UID: $userUid")

                            NavGraph(
                                navController = navController,
                                workoutViewModel = workoutViewModel.apply { setUserId(currentUserUid) },
                                dietViewModel = dietViewModel.apply { setUserId(currentUserUid) },
                                sleepViewModel = sleepViewModel.apply { setUserId(currentUserUid) }
                            )
                        } else {
                            AuthNavGraph(
                                navController = navController,
                                loginViewModel = loginViewModel,
                                signUpViewModel = signUpViewModel
                            )
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
