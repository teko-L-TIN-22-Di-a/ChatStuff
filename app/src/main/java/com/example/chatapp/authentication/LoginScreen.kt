package com.example.chatapp.authentication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.R

@Composable
fun LoginScreen(
    startScreen: () -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
    val loginState by loginViewModel.loginState.collectAsState()
    val email: String = loginState.email
    val password: String = loginState.password

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            TextField(
                value = email,
                onValueChange = { loginViewModel.setEmail(it) },
                label = { Text("E-Mail") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                isError = email.isEmpty()
            )
            TextField(
                value = password,
                onValueChange = { loginViewModel.setPassword(it) },
                label = { Text("Passwort") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = password.isEmpty()
            )
            Spacer(modifier = Modifier.height(20.dp))
            Button(
                onClick = { loginViewModel.login(startScreen = startScreen) },
                enabled = email.isNotEmpty() && password.isNotEmpty()
            ) {
                Row {
                    Text(text = "Login")
                    if(loginState.loading) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(16.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                }
            }
        }
    }
}