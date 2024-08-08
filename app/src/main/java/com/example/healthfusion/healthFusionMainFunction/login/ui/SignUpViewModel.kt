package com.example.healthfusion.healthFusionMainFunction.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.healthFusionMainFunction.login.di.LoginRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _signUpState = MutableStateFlow<AuthState>(AuthState.Idle)
    val signUpState: StateFlow<AuthState> = _signUpState

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _signUpState.value = AuthState.Idle
            val emailError = validateEmail(email)
            val passwordError = validatePassword(password)
            if (emailError != null || passwordError != null) {
                _signUpState.value =
                    AuthState.Error(emailError = emailError, passwordError = passwordError)
                return@launch
            }

            try {
                loginRepository.signUp(email, password)
                _signUpState.value = AuthState.Success
            } catch (e: Exception) {
                _signUpState.value = firebaseException(e)
            }
        }
    }

    private fun firebaseException(e: Exception): AuthState {
        return when (e) {
            is FirebaseAuthInvalidUserException -> AuthState.AuthError("Invalid email address.")
            is FirebaseAuthInvalidCredentialsException -> AuthState.AuthError("Invalid password.")
            else -> AuthState.AuthError(e.localizedMessage ?: "An unexpected error occurred.")
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

    fun resetState() {
        _signUpState.value = AuthState.Idle
    }
}