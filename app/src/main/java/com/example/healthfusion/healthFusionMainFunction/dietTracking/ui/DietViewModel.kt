package com.example.healthfusion.healthFusionMainFunction.dietTracking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.Diet
import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.DietDao
import com.example.healthfusion.healthFusionMainFunction.login.di.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DietViewModel @Inject constructor(
    private val dietDao: DietDao,
    private val loginRepository: LoginRepository

) : ViewModel() {

    // get the user UID
    private val currentUserUid: String? = loginRepository.getCurrentUser()?.uid

    val diets: StateFlow<List<Diet>> = currentUserUid?.let { uid ->
        dietDao.getDietForUser(uid).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    } ?: MutableStateFlow(emptyList())


    fun addDiet(name: String, calories: Int) {
        viewModelScope.launch {
            currentUserUid?.let { uid ->
                val diet = Diet(
                    name = name, calories = calories, userId = uid
                )
                dietDao.insert(diet)
            }

        }

    }
}