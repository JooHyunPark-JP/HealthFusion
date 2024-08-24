package com.example.healthfusion.healthFusionMainFunction.dietTracking.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.healthFusionData.fireStore.FirestoreRepository
import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.Diet
import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.DietDao
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
class DietViewModel @Inject constructor(
    private val dietDao: DietDao,
    private val firestoreRepository: FirestoreRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    private val _userId = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val diets: StateFlow<List<Diet>> = _userId.flatMapLatest { uid ->
        uid?.let {
            dietDao.getDietForUser(it)
        } ?: flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addDiet(name: String, calories: Int) {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                var diet = Diet(
                    name = name, calories = calories, userId = uid, isSynced = false
                )
                // Save data into Room database regardless of network connection
                val insertedId = dietDao.insert(diet)
                diet = diet.copy(id = insertedId.toInt()) // update ID after insertion

                if (networkHelper.isNetworkConnected()) {
                    try {
                        // Save diet data into Firestore
                        firestoreRepository.saveDiet(uid, diet.copy(isSynced = true))
                        dietDao.update(diet.copy(isSynced = true))
                    } catch (e: Exception) {
                        Log.e(
                            "SyncError",
                            "Failed to sync diet to Firestore: ${e.localizedMessage}"
                        )
                    }
                }
            }
        }
    }

    fun syncUnsyncedDiets() {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                val unsyncedDiets = dietDao.getUnsyncedDiets(uid)
                unsyncedDiets.forEach { diet ->
                    try {
                        firestoreRepository.saveDiet(uid, diet)
                        dietDao.update(diet.copy(isSynced = true))
                    } catch (e: Exception) {
                        Log.e("SyncError", "Failed to sync diet: ${e.localizedMessage}")
                    }
                }
            }
        }
    }

    fun syncDietFromFirestore() {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                if (networkHelper.isNetworkConnected()) {
                    try {
                        val firestoreDiets = firestoreRepository.getDietsFromFirestore(uid)
                        firestoreDiets.forEach { diet ->
                            val existingDiet = dietDao.getDietById(diet.id)
                            if (existingDiet == null) {
                                dietDao.insert(diet.copy(isSynced = true))
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("SyncError", "Failed to sync workouts from Firestore: ${e.localizedMessage}")
                    }
                }
            }
        }
    }

    fun setUserId(uid: String?) {
        _userId.value = uid
    }
}