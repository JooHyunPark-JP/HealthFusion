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
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLogin by remember { mutableStateOf(true) }
    val loginState by viewModel.loginState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Login", fontSize = 24.sp)

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = loginState is LoginState.Error && (loginState as LoginState.Error).emailError != null,
            supportingText = {
                if (loginState is LoginState.Error)
                    Text(
                        (loginState as LoginState.Error).emailError ?: ""
                    )
            }

        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            isError = loginState is LoginState.Error && (loginState as LoginState.Error).passwordError != null,
            supportingText = {
                if (loginState is LoginState.Error) Text(
                    (loginState as LoginState.Error).passwordError ?: ""
                )
            }
        )

        Button(
            onClick = {
                if (isLogin) {
                    viewModel.login(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        when (loginState) {
            is LoginState.AuthError -> {
                Spacer(modifier = Modifier.height(8.dp))
                if ((loginState as LoginState.AuthError).authError != null) {
                    Text(
                        text = (loginState as LoginState.AuthError).authError
                            ?: "An unexpected error occurred",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is LoginState.Success -> {
                // Handle successful login if needed ex: Add login success message etc.
            }

            is LoginState.Idle -> { /* Do nothing */
            }

            is LoginState.Error -> { /* Do nothing */
            }
        }


        TextButton(onClick = { navController.navigate("signup") }) {
            Text("Don't have an account? Sign Up")
        }
    }
}