package com.example.healthfusion.util

import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NetworkCallback(
    private val onNetworkAvailable: () -> Unit,
    private val onNetworkLost: () -> Unit
) : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        CoroutineScope(Dispatchers.Main).launch {
            onNetworkAvailable()
        }
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        onNetworkLost()
    }
}