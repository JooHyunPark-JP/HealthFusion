package com.example.healthfusion.healthFusionData.fireStore

import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.Workout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    fun addWorkout(userId: String, workout: Workout): Flow<Result<Unit>> = flow {
        val workoutRef = firestore.collection("users").document(userId).collection("workouts")
        workoutRef.add(workout).await()
        emit(Result.success(Unit))
    }.catch { e ->
        emit(Result.failure(e))
    }

    fun getWorkouts(userId: String): Flow<List<Workout>> = flow {
        val workoutRef = firestore.collection("users").document(userId).collection("workouts")
        val snapshots = workoutRef.get().await()
        val workouts = snapshots.toObjects(Workout::class.java)
        emit(workouts)
    }.catch { e ->
        emit(emptyList())
    }
}