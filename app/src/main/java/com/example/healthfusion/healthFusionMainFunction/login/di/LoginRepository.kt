package com.example.healthfusion.healthFusionMainFunction.login.di

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun login(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun signUp(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).await()
    }
}