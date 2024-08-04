package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.Workout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutDao: WorkoutDao
) : ViewModel() {

    //convert flow to stateFlow by using stainIn
    val workouts: StateFlow<List<Workout>> = workoutDao.getAllExercises()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun addWorkout(workout: Workout) {
        viewModelScope.launch {
            workoutDao.insert(workout)
        }
    }
}