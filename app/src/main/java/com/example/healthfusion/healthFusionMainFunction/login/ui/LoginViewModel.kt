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
            try {
                loginRepository.login(email, password)
                _loginState.value = LoginState.Success
            } catch (e: Exception) {
                _loginState.value = when (e) {
                    is FirebaseAuthInvalidUserException -> LoginState.Error("Invalid email address.")
                    is FirebaseAuthInvalidCredentialsException -> LoginState.Error("Invalid password.")
                    else -> LoginState.Error(e.localizedMessage ?: "An unexpected error occurred.")
                }
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                loginRepository.signUp(email, password)
                _loginState.value = LoginState.Success
            } catch (e: Exception) {
                _loginState.value =
                    LoginState.Error(e.localizedMessage ?: "An unexpected error occurred.")
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}