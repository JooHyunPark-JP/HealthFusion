package com.example.healthfusion

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
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
import com.example.healthfusion.healthFusionNav.TopBar
import com.example.healthfusion.ui.theme.HealthFusionTheme
import com.example.healthfusion.util.NetworkCallback
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
    private lateinit var networkCallback: NetworkCallback
    private lateinit var connectivityManager: ConnectivityManager

    @Inject
    lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            //Check the current device internet connection. When network is back online, try unsyncedData.
            networkCallback = NetworkCallback(
                onNetworkAvailable = {
                    syncRoomDatabaseAndFirestoreData()
                },
                onNetworkLost = {
                    // if network is disconnected, add extra work here.
                }
            )

            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            connectivityManager.registerNetworkCallback(request, networkCallback)

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
                    topBar = {
                        TopBar(navController)
                    },
                    bottomBar = {
                        if (currentUser.value != null) {
                            BottomNavBar(navController = navController)
                        }
                    }) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        //If user is already logged in
                        if (currentUser.value != null) {
                            /*
                                                        val userUid = currentUser.value?.uid

                                                        // checking user UID for debugging
                                                        Log.d("CheckingUID", "User UID: $userUid")
                                                        Log.d("checkingloginUID", "user UID from loginModel: $currentUserUid ")*/

                            // Set user ID and let the ViewModel handle syncing
                            workoutViewModel.setUserId(currentUserUid)
                            dietViewModel.setUserId(currentUserUid)
                            sleepViewModel.setUserId(currentUserUid)

                            NavGraph(
                                navController = navController,
                                workoutViewModel = workoutViewModel,
                                dietViewModel = dietViewModel,
                                sleepViewModel = sleepViewModel,
                                loginViewModel = loginViewModel
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

    private fun syncRoomDatabaseAndFirestoreData() {
        workoutViewModel.syncUnsyncedWorkouts()
        workoutViewModel.syncWorkoutsFromFirestore()
        sleepViewModel.syncUnsyncedSleepRecords()
        sleepViewModel.syncSleepsFromFirestore()
        dietViewModel.syncUnsyncedDiets()
        dietViewModel.syncDietFromFirestore()
    }

    override fun onDestroy() {
        super.onDestroy()
        // remove receiver
        connectivityManager.unregisterNetworkCallback(networkCallback)
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
