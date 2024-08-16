package com.example.healthfusion.healthFusionMainFunction.sleepTracking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.healthFusionMainFunction.login.di.LoginRepository
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.Sleep
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.SleepDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SleepViewModel @Inject constructor(
    private val sleepDao: SleepDao,
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _userId = MutableStateFlow<String?>(null)


    @OptIn(ExperimentalCoroutinesApi::class)
    val sleepRecords: StateFlow<List<Sleep>> = _userId.flatMapLatest { uid ->
        uid?.let {
            sleepDao.getSleepForUser(it)
        } ?: flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun addSleepRecord(date: String, startTime: String, endTime: String, quality: Int) {
        viewModelScope.launch {
            _userId.value?.let { uid ->
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

    fun setUserId(uid: String?) {
        _userId.value = uid
    }
}