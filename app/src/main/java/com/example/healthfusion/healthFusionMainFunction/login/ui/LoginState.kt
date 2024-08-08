package com.example.healthfusion.healthFusionMainFunction.login.ui

sealed class LoginState {
    data object Idle : LoginState()
    data object Success : LoginState()
    data class Error(val emailError: String? = null, val passwordError: String? = null) :
        LoginState()

    data class AuthError(val authError: String? = null) : LoginState()
}