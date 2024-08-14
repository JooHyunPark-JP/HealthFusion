package com.example.healthfusion.healthFusionMainFunction.login.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun SignUpScreen(
    navController: NavHostController,
    viewModel: SignUpViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val signUpState by viewModel.signUpState.collectAsState()

    //Remove error exception message when navigated from another page to signup page
    DisposableEffect(Unit) {
        val callback = NavController.OnDestinationChangedListener { _, _, _ ->
            viewModel.resetState()

        }
        navController.addOnDestinationChangedListener(callback)
        onDispose {
            navController.removeOnDestinationChangedListener(callback)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Sign Up!", fontSize = 24.sp)

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = signUpState is AuthState.Error && (signUpState as AuthState.Error).emailError != null,
            supportingText = {
                if (signUpState is AuthState.Error)
                    Text(
                        (signUpState as AuthState.Error).emailError ?: ""
                    )
            }
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = signUpState is AuthState.Error && (signUpState as AuthState.Error).passwordError != null,
            supportingText = {
                if (signUpState is AuthState.Error) Text(
                    (signUpState as AuthState.Error).passwordError ?: ""
                )
            }
        )

        Button(
            onClick = {
                viewModel.signUp(email, password)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign Up!")
        }


        when (signUpState) {
            is AuthState.AuthError -> {
                Spacer(modifier = Modifier.height(8.dp))
                if ((signUpState as AuthState.AuthError).authError != null) {
                    Text(
                        text = (signUpState as AuthState.AuthError).authError
                            ?: "An unexpected error occurred",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is AuthState.Success -> {
                // Handle successful login if needed ex: Add login success message etc.
            }

            is AuthState.Idle -> { /* Do nothing */
            }

            is AuthState.Error -> { /* Do nothing */
            }
        }


        TextButton(onClick = { navController.navigate("login") }) {
            Text("Already have an account? Login")
        }
    }
}