package com.example.healthfusion.healthFusionMainFunction.workoutTracking.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui.WorkoutViewModel

class NetworkChangeReceiver(private val workoutViewModel: WorkoutViewModel) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.extras != null) {
            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            if (networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
                // if network is back, try sync.
                workoutViewModel.syncUnsyncedWorkouts()
            }
        }
    }
}