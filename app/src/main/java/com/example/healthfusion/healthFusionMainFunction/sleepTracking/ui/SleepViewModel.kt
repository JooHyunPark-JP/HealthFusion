package com.example.healthfusion.healthFusionMainFunction.sleepTracking.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.healthFusionData.fireStore.FirestoreRepository
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.Sleep
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.SleepDao
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.toDTO
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.toEntity
import com.example.healthfusion.util.DateFormatter
import com.example.healthfusion.util.NetworkHelper
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
    private val firestoreRepository: FirestoreRepository,
    private val networkHelper: NetworkHelper,
    private val dateFormatter: DateFormatter
) : ViewModel() {

    private val _userId = MutableStateFlow<String?>(null)

    // get the sleep data of current user and convert flow to stateFlow by using stateIn
    @OptIn(ExperimentalCoroutinesApi::class)
    val sleepRecords: StateFlow<List<Sleep>> = _userId.flatMapLatest { uid ->
        uid?.let {
            sleepDao.getSleepForUser(it)
        } ?: flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun addSleepRecord(date: String, startTime: String, endTime: String, quality: Int) {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                var sleep = Sleep(
                    date = date,
                    startTime = startTime,
                    endTime = endTime,
                    quality = quality,
                    userId = uid,
                    isSynced = false,
                    lastModified = System.currentTimeMillis()
                )
                // Save data into Room database regardless of network connection
                val insertedId = sleepDao.insert(sleep)
                sleep = sleep.copy(id = insertedId.toInt()) // update ID after insertion

                if (networkHelper.isNetworkConnected()) {
                    try {
                        val sleepDTO = sleep.toDTO(dateFormatter)
                        // Save sleep data into Firestore
                        firestoreRepository.saveSleep(uid, sleepDTO)
                        sleepDao.update(sleep.copy(isSynced = true))
                    } catch (e: Exception) {
                        Log.e(
                            "SyncError",
                            "Failed to sync sleep to Firestore: ${e.localizedMessage}"
                        )
                    }
                }
            }
        }
    }


    fun syncUnsyncedSleepRecords() {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                val unsyncedSleeps = sleepDao.getUnsyncedSleeps(uid)
                unsyncedSleeps.forEach { sleep ->
                    try {
                        val sleepDTO = sleep.toDTO(dateFormatter)
                        firestoreRepository.saveSleep(uid, sleepDTO)
                        sleepDao.update(sleep.copy(isSynced = true))
                    } catch (e: Exception) {
                        Log.e("SyncError", "Failed to sync sleep record: ${e.localizedMessage}")
                    }
                }
            }
        }

    }

    fun syncSleepsFromFirestore() {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                if (networkHelper.isNetworkConnected()) {
                    try {
                        val firestoreSleeps = firestoreRepository.getSleepsFromFirestore(uid)
                        firestoreSleeps.forEach { fireStoreSleep ->
                            val existingSleep = sleepDao.getSleepById(fireStoreSleep.id)
                            val firestoreLastModified =
                                dateFormatter.parseDateTimeToMillis(fireStoreSleep.lastModified)
                            if (existingSleep == null) {
                                sleepDao.insert(
                                    fireStoreSleep.toEntity(dateFormatter).copy(isSynced = true)
                                )
                            } else if (firestoreLastModified != null) {
                                if (firestoreLastModified > existingSleep.lastModified) {
                                    sleepDao.update(
                                        fireStoreSleep.toEntity(dateFormatter).copy(isSynced = true)
                                    )
                                }
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(
                            "SyncError",
                            "Failed to sync workouts from Firestore: ${e.localizedMessage}"
                        )
                    }
                }
            }
        }
    }

    fun setUserId(uid: String?) {
        _userId.value = uid

        if (uid != null) {
            syncSleepRoomDatabaseAndFirestoreData()
        }
    }

    private fun syncSleepRoomDatabaseAndFirestoreData() {
        syncUnsyncedSleepRecords()
        syncSleepsFromFirestore()
    }
}