package com.example.healthfusion.healthFusionMainFunction.dietTracking.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.Diet
import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.DietDao
import com.example.healthfusion.healthFusionMainFunction.login.di.LoginRepository
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
class DietViewModel @Inject constructor(
    private val dietDao: DietDao,
    private val loginRepository: LoginRepository

) : ViewModel() {

    private val _userId = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val diets: StateFlow<List<Diet>> = _userId.flatMapLatest { uid ->
        uid?.let {
            dietDao.getDietForUser(it)
        } ?: flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // get the user UID
    private val currentUserUid: String? = loginRepository.getCurrentUser()?.uid


    fun addDiet(name: String, calories: Int) {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                val diet = Diet(
                    name = name, calories = calories, userId = uid
                )
                dietDao.insert(diet)
            }
        }
    }

    fun setUserId(uid: String?) {
        _userId.value = uid
    }
}