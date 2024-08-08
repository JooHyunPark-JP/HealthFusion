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
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Idle
            val emailError = validateEmail(email)
            val passwordError = validatePassword(password)
            if (emailError != null || passwordError != null) {
                _loginState.value =
                    LoginState.Error(emailError = emailError, passwordError = passwordError)
                return@launch
            }
            try {
                loginRepository.login(email, password)
                _loginState.value = LoginState.Success
            } catch (e: Exception) {
                firebaseException(e)
            }

        }
    }


    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Idle
            val emailError = validateEmail(email)
            val passwordError = validatePassword(password)
            if (emailError != null || passwordError != null) {
                _loginState.value =
                    LoginState.Error(emailError = emailError, passwordError = passwordError)
                return@launch
            }
            try {
                loginRepository.signUp(email, password)
                _loginState.value = LoginState.Success
            } catch (e: Exception) {
                firebaseException(e)
            }
        }
    }


    private fun firebaseException(e: Exception) {
        //Add more firebase exception when needed
        when (e) {
            is FirebaseAuthInvalidUserException -> LoginState.AuthError("Invalid email address.")
            is FirebaseAuthInvalidCredentialsException -> LoginState.AuthError("Invalid password.")
            else -> LoginState.AuthError(
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


    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}