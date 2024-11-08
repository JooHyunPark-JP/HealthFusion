package com.example.healthfusion.healthFusionMainFunction.profile.ui


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.healthfusion.healthFusionMainFunction.login.ui.LoginViewModel
import com.example.healthfusion.ui.theme.HealthFusionTheme

@Composable
fun ProfileScreen(loginViewModel: LoginViewModel) {
    HealthFusionTheme {


        Column(
            horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(text = "More Profile features will be updated.")


            Spacer(modifier = Modifier.height(20.dp))
            //temporary sign out function with button
            Button(
                onClick = {
                    loginViewModel.logout()
                }
            ) {
                Text("Sign Out")
            }
        }
    }

}