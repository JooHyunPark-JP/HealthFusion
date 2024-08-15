package com.example.healthfusion.healthFusionMainFunction.sleepTracking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.healthFusionMainFunction.login.di.LoginRepository
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.Sleep
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.SleepDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SleepViewModel @Inject constructor(
    private val sleepDao: SleepDao,
    private val loginRepository: LoginRepository
) : ViewModel() {

    // get the user UID
    private val currentUserUid: String? = loginRepository.getCurrentUser()?.uid

    val sleepRecords: StateFlow<List<Sleep>> = currentUserUid?.let { uid ->
        sleepDao.getSleepForUser(uid).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    } ?: MutableStateFlow(emptyList())


    fun addSleepRecord(date: String, startTime: String, endTime: String, quality: Int) {
        viewModelScope.launch {
            currentUserUid?.let { uid ->
                val sleep = Sleep(
                    date = date,
                    startTime = startTime,
                    endTime = endTime,
                    quality = quality,
                    userId = uid
                )
                sleepDao.insert(sleep)
            }
        }
    }
}