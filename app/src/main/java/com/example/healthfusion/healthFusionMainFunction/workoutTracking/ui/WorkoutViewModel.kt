package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.healthFusionMainFunction.login.di.LoginRepository
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.Workout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutDao
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val loginRepository: LoginRepository
) : ViewModel() {

    // get the user UID
    private val currentUserUid: String? = loginRepository.getCurrentUser()?.uid

    // get the workout data of current user and convert flow to stateFlow by using stainIn
    val workouts: StateFlow<List<Workout>> = currentUserUid?.let { uid ->
        workoutDao.getWorkoutsForUser(uid)
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    } ?: MutableStateFlow(emptyList())


    fun addWorkout(name: String, duration: Int, caloriesBurned: Int, type: WorkoutType) {
        viewModelScope.launch {
            currentUserUid?.let { uid ->
                val workout = Workout(
                    name = name,
                    duration = duration,
                    caloriesBurned = caloriesBurned,
                    type = type,
                    userId = uid
                )
                workoutDao.insert(workout.copy(userId = uid))
            }
        }
    }


}
