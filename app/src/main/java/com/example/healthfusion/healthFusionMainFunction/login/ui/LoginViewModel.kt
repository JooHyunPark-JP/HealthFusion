package com.example.healthfusion.healthFusionMainFunction.login.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.healthFusionMainFunction.login.di.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _currentUserUid = MutableStateFlow<String?>(null)
    val currentUserUid: StateFlow<String?> = _currentUserUid.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState


    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        firebaseAuth.addAuthStateListener { auth ->
            _currentUserUid.value = auth.currentUser?.uid
            _authState.value = if (auth.currentUser != null) AuthState.Success else AuthState.Idle
            Log.d("checkinginLoginUserUid", "$_currentUserUid ")
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Idle
            val emailError = validateEmail(email)
            val passwordError = validatePassword(password)
            if (emailError != null || passwordError != null) {
                _authState.value =
                    AuthState.Error(emailError = emailError, passwordError = passwordError)
                return@launch
            }
            try {
                loginRepository.login(email, password)
                _currentUserUid.value = loginRepository.getCurrentUser()?.uid
                _authState.value = AuthState.Success
            } catch (e: Exception) {
                _authState.value = firebaseException(e)
            }

        }
    }

    private fun firebaseException(e: Exception): AuthState {
        //Add more firebase exception when needed
        return when (e) {
            is FirebaseAuthInvalidUserException -> AuthState.AuthError("Invalid email address.")
            is FirebaseAuthInvalidCredentialsException -> AuthState.AuthError("Invalid password.")
            else -> AuthState.AuthError(
                e.localizedMessage ?: "An unexpected error occurred."
            )
        }
    }

    private fun validateEmail(email: String): String? {
        return if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            "Please enter a valid email address."
        } else null
    }

    private fun validatePassword(password: String): String? {
        return if (password.length < 6) {
            "Password must be at least 6 characters."
        } else null
    }

    fun logout() {
        //sign out the current ID
        firebaseAuth.signOut()
        _currentUserUid.value = null
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }
}