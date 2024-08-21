package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.healthFusionData.fireStore.FirestoreRepository
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.Workout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutDao
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.util.NetworkHelper
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
class WorkoutViewModel @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val firestoreRepository: FirestoreRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _userId = MutableStateFlow<String?>(null)

    // get the workout data of current user and convert flow to stateFlow by using stainIn
    @OptIn(ExperimentalCoroutinesApi::class)
    val workouts: StateFlow<List<Workout>> = _userId.flatMapLatest { uid ->
        uid?.let {
            workoutDao.getWorkoutsForUser(it)
        } ?: flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun addWorkout(name: String, duration: Int, caloriesBurned: Int, type: WorkoutType) {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                val workout = Workout(
                    name = name,
                    duration = duration,
                    caloriesBurned = caloriesBurned,
                    type = type,
                    userId = uid,
                    isSynced = false
                )
                //save data into room database regardless of network connection
                workoutDao.insert(workout)

                if (networkHelper.isNetworkConnected()) {
                    try {
                        // Save workout data into firestore
                        firestoreRepository.saveWorkout(uid, workout)
                        workoutDao.update(workout.copy(isSynced = true))
                    } catch (e: Exception) {
                        Log.e(
                            "SyncError",
                            "Failed to sync workout to Firestore: ${e.localizedMessage}"
                        )
                    }
                }
            }
        }
    }

    fun syncUnsyncedWorkouts() {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                val unsyncedWorkouts = workoutDao.getUnsyncedWorkouts(uid)
                unsyncedWorkouts.forEach { workout ->
                    try {
                        firestoreRepository.saveWorkout(uid, workout)
                        workoutDao.update(workout.copy(isSynced = true))
                    } catch (e: Exception) {
                        Log.e("SyncError", "Failed to sync workout: ${e.localizedMessage}")
                    }
                }
            }
        }
    }


    fun setUserId(uid: String?) {
        _userId.value = uid
    }

}
