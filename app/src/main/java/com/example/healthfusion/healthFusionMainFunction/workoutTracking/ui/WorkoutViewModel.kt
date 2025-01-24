package com.example.healthfusion.healthFusionMainFunction.workoutTracking.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.healthFusionData.fireStore.FirestoreRepository
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.Workout
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutDao
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoal
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoalDao
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutGoalType
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutType
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.toDTO
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.toEntity
import com.example.healthfusion.util.DateFormatter
import com.example.healthfusion.util.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
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
    private val workoutGoalDao: WorkoutGoalDao,
    private val firestoreRepository: FirestoreRepository,
    private val networkHelper: NetworkHelper,
    private val dateFormatter: DateFormatter
) : ViewModel() {

    private val _userId = MutableStateFlow<String?>(null)


    // get the workout data of current user and convert flow to stateFlow by using stainIn
    @OptIn(ExperimentalCoroutinesApi::class)
    val workouts: StateFlow<List<Workout>> = _userId.flatMapLatest { uid ->
        uid?.let {
            workoutDao.getWorkoutsForUser(it)
        } ?: flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    //daily goals
    @OptIn(ExperimentalCoroutinesApi::class)
    val dailyGoals: StateFlow<List<WorkoutGoal>> = _userId.flatMapLatest { uid ->
        uid?.let {
            workoutGoalDao.getGoalsForUserAndType(it, WorkoutGoalType.DAILY)
        } ?: flowOf(emptyList())
    }.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        emptyList()
    )

    // Weekly goals
    @OptIn(ExperimentalCoroutinesApi::class)
    val weeklyGoals: StateFlow<List<WorkoutGoal>> = _userId.flatMapLatest { uid ->
        uid?.let {
            workoutGoalDao.getGoalsForUserAndType(it, WorkoutGoalType.WEEKLY)
        } ?: flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())


    fun addWorkout(
        name: String,
        duration: Int?,
        caloriesBurned: Int?,
        type: WorkoutType,
        distance: Int?,
        set: Int?,
        repetition: Int?,
        weight: Int?,
        workoutDate: Long,
        equipmentType: String?,
        gripStyle: String?
    ) {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                var workout = Workout(
                    name = name,
                    type = type,

                    duration = duration,
                    caloriesBurned = caloriesBurned,
                    distance = distance,

                    set = set,
                    repetition = repetition,
                    weight = weight,
                    equipmentType = equipmentType,
                    gripStyle = gripStyle,

                    userId = uid,
                    workoutDate = workoutDate,
                    isSynced = false,
                    lastModified = System.currentTimeMillis()
                )
                //save data into room database regardless of network connection
                val insertedId = workoutDao.insert(workout)
                workout = workout.copy(id = insertedId.toInt()) // need to update ID

                if (networkHelper.isNetworkConnected()) {
                    try {
                        val workoutDTO = workout.toDTO(dateFormatter)
                        // Save workout data into firestore
                        firestoreRepository.saveWorkout(uid, workoutDTO)
                        workoutDao.update(workout.copy(isSynced = true))

                        /* // Testing purpose: after update, view the database data
                       val workouts = workoutDao.getWorkoutsForUser(uid)
                       .firstOrNull()

                     // all workout data review
                       Log.d("Testing workoutData2", "Workouts for user $workouts")*/
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


    @OptIn(ExperimentalCoroutinesApi::class)
    fun getFilteredWorkoutsByName(workoutName: String?): Flow<List<Workout>> {
        return _userId.flatMapLatest { uid ->
            if (uid == null || workoutName.isNullOrEmpty()) {
                flowOf(emptyList())
            } else {
                workoutDao.getWorkoutsByName(uid, workoutName)
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    }

    fun deleteWorkout(workout: Workout) {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                try {
                    // Remove from Firestore if connected to the network
                    if (networkHelper.isNetworkConnected()) {
                        firestoreRepository.deleteWorkout(uid, workout.id.toString())
                    }
                    // Always delete from Room database
                    workoutDao.delete(workout)
                } catch (e: Exception) {
                    Log.e("DeleteError", "Failed to delete workout: ${e.localizedMessage}")
                }
            }
        }
    }

    fun addWorkoutGoal(text: String) {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                val newGoal = WorkoutGoal(
                    text = text,
                    isCompleted = false,
                    userId = uid,
                    type = WorkoutGoalType.DAILY
                )
                workoutGoalDao.insert(newGoal)
            }
        }
    }

    // Weekly goals 추가
    fun addWeeklyGoal(text: String) {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                val newGoal = WorkoutGoal(
                    text = text,
                    isCompleted = false,
                    userId = uid,
                    type = WorkoutGoalType.WEEKLY
                )
                workoutGoalDao.insert(newGoal)
            }
        }
    }

    fun updateWorkoutGoal(workoutGoal: WorkoutGoal) {
        viewModelScope.launch {
            workoutGoalDao.update(workoutGoal)
        }
    }

    fun deleteWorkoutGoal(workoutGoal: WorkoutGoal) {
        viewModelScope.launch {
            workoutGoalDao.delete(workoutGoal)
        }
    }

    //If room database has data that firestore hasn't, sync the data
    fun syncUnsyncedWorkouts() {
        viewModelScope.launch {
            _userId.value?.let { uid ->
                val unsyncedWorkouts = workoutDao.getUnsyncedWorkouts(uid)
                unsyncedWorkouts.forEach { workout ->
                    try {
                        val workoutDTO = workout.toDTO(dateFormatter)
                        firestoreRepository.saveWorkout(uid, workoutDTO)
                        workoutDao.update(workout.copy(isSynced = true))
                    } catch (e: Exception) {
                        Log.e("SyncError", "Failed to sync workout: ${e.localizedMessage}")
                    }
                }
            }
        }
    }

    //when firestore has data but room database hasn't, get data from firestore and update room database
    fun syncWorkoutsFromFirestore() {
        Log.d("checkingloginUID2", "${_userId.value} ")
        viewModelScope.launch {
            _userId.value?.let { uid ->
                if (networkHelper.isNetworkConnected()) {
                    try {
                        val firestoreWorkouts = firestoreRepository.getWorkoutsFromFirestore(uid)
                        firestoreWorkouts.forEach { firestoreWorkout ->
                            val existingWorkout = workoutDao.getWorkoutById(firestoreWorkout.id)
                            val firestoreLastModified =
                                dateFormatter.parseDateTimeToMillis(firestoreWorkout.lastModified)
                            if (existingWorkout == null) {
                                workoutDao.insert(
                                    firestoreWorkout.toEntity(dateFormatter).copy(isSynced = true)
                                )
                            }
                            //if firestore data is recent then update room database data
                            else if (firestoreLastModified != null) {
                                if (firestoreLastModified > existingWorkout.lastModified) {
                                    workoutDao.update(
                                        firestoreWorkout.toEntity(dateFormatter)
                                            .copy(isSynced = true)
                                    )
                                }
                            }
                            //else (firestore data is older one than room database, do nothing (for now)
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

        // Trigger syncing once the userId is set
        if (uid != null) {
            syncWorkoutRoomDatabaseAndFirestoreData()
        }
    }

    private fun syncWorkoutRoomDatabaseAndFirestoreData() {
        syncUnsyncedWorkouts()
        syncWorkoutsFromFirestore()
    }

}
