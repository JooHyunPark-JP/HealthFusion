package com.example.healthfusion.healthFusionData.fireStore

import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.Diet
import com.example.healthfusion.healthFusionMainFunction.login.data.User
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.Sleep
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.Workout
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    // save workout data into firestore
    suspend fun saveWorkout(userId: String, workout: Workout) {
        val docRef = firestore.collection("users").document(userId)
            .collection("workouts").document()
        docRef.set(workout).await()
    }


    suspend fun saveDiet(userId: String, diet: Diet) {
        val docRef = firestore.collection("users").document(userId)
            .collection("diets").document()
        docRef.set(diet).await()
    }


    suspend fun saveSleep(userId: String, sleep: Sleep) {
        val docRef = firestore.collection("users").document(userId)
            .collection("sleeps").document()
        docRef.set(sleep).await()
    }

    suspend fun saveUser(userId: String, user: User): Result<Unit> {
        return try {
            firestore.collection("users").document(userId).set(user).await()

            firestore.collection("users").document(userId)
                .update("createdAt", FieldValue.serverTimestamp()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}