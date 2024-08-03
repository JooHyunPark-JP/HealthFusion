package com.example.healthfusion.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.data.Exercise
import com.example.healthfusion.data.ExerciseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(
    private val exerciseDao: ExerciseDao
) : ViewModel() {

    //convert flow to stateFlow by using stainIn
    val exercises: StateFlow<List<Exercise>> = flow {
        emit(exerciseDao.getAllExercises())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun addExercise(exercise: Exercise) {
        viewModelScope.launch {
            exerciseDao.insert(exercise)
        }
    }
}