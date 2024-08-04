package com.example.healthfusion.healthFusionMainFunction.sleepTracking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.Sleep
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.SleepDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SleepViewModel @Inject constructor(
    private val sleepDao: SleepDao
) : ViewModel() {

    val sleepRecords: StateFlow<List<Sleep>> = sleepDao.getAllSleepRecords()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addSleepRecord(sleep: Sleep) {
        viewModelScope.launch {
            sleepDao.insert(sleep)
        }
    }
}