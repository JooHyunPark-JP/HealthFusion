package com.example.healthfusion.healthFusionData.fireStore

import android.util.Log
import com.example.healthfusion.healthFusionMainFunction.dietTracking.data.DietDTO
import com.example.healthfusion.healthFusionMainFunction.login.data.User
import com.example.healthfusion.healthFusionMainFunction.sleepTracking.data.SleepDTO
import com.example.healthfusion.healthFusionMainFunction.workoutTracking.data.WorkoutDTO
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
    suspend fun saveWorkout(userId: String, workoutDTO: WorkoutDTO): Result<Unit> {
        return try {
            val docId = workoutDTO.id.toString() // Use workout ID as Firestore document ID
            firestore.collection("users").document(userId)
                .collection("workouts").document(docId).set(workoutDTO).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveDiet(userId: String, dietDTO: DietDTO): Result<Unit> {
        return try {
            val docRef = firestore.collection("users").document(userId)
                .collection("diets").document()
            docRef.set(dietDTO).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveSleep(userId: String, sleepDTO: SleepDTO): Result<Unit> {
        return try {
            val docRef = firestore.collection("users").document(userId)
                .collection("sleeps").document()
            docRef.set(sleepDTO).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
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


    suspend fun getWorkoutsFromFirestore(userId: String): List<WorkoutDTO> {
        return try {
            val snapshot = firestore.collection("users").document(userId)
                .collection("workouts").get().await()
            snapshot.toObjects(WorkoutDTO::class.java)
        } catch (e: Exception) {
            Log.e("FirestoreError", "Failed to fetch workouts: ${e.localizedMessage}")
            emptyList()
        }
    }

    suspend fun getSleepsFromFirestore(userId: String): List<SleepDTO> {
        return try {
            val snapshot = firestore.collection("users").document(userId)
                .collection("sleeps").get().await()
            snapshot.toObjects(SleepDTO::class.java)
        } catch (e: Exception) {
            Log.e("FirestoreError", "Failed to fetch sleeps: ${e.localizedMessage}")
            emptyList()
        }
    }

    suspend fun getDietsFromFirestore(userId: String): List<DietDTO> {
        return try {
            val snapshot = firestore.collection("users").document(userId)
                .collection("diets").get().await()
            snapshot.toObjects(DietDTO::class.java)
        } catch (e: Exception) {
            Log.e("FirestoreError", "Failed to fetch diets: ${e.localizedMessage}")
            emptyList()
        }
    }

    suspend fun deleteWorkout(userId: String, workoutId: String): Result<Unit> {
        return try {
            firestore.collection("users").document(userId)
                .collection("workouts").document(workoutId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("FirestoreError", "Failed to delete workout: ${e.localizedMessage}")
            Result.failure(e)
        }
    }

}