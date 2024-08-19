package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.healthFusionData.fireStore.FirestoreRepository
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.Workout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutDao
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
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
    private val firestoreRepository: FirestoreRepository
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
                    userId = uid
                )
                workoutDao.insert(workout)

                // Save workout data into firestore
                firestoreRepository.saveWorkout(uid, workout)
            }
        }
    }

    fun setUserId(uid: String?) {
        _userId.value = uid
    }

}
