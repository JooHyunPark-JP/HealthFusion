package com.example.healthfusion.healthFusionMainFunction.login.ui

sealed class AuthState {
    data object Idle : AuthState()
    data object Success : AuthState()
    data class Error(val emailError: String? = null, val passwordError: String? = null) :
        AuthState()

    data class AuthError(val authError: String? = null) : AuthState()
}