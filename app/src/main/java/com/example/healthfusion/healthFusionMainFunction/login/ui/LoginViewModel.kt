package com.example.healthfusion.healthFusionMainFunction.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthfusion.healthFusionMainFunction.login.di.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : ViewModel() {

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginRepository.login(email, password)
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            loginRepository.signUp(email, password)
        }
    }
}