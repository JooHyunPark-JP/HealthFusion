package com.example.healthfusion.healthFusionMainFunction.dietTracking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.Diet
import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.DietDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DietViewModel @Inject constructor(
    private val dietDao: DietDao
) : ViewModel() {

    val diets: StateFlow<List<Diet>> = dietDao.getAllDiets()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addDiet(diet: Diet) {
        viewModelScope.launch {
            dietDao.insert(diet)
        }
    }
}