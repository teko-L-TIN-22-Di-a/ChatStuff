package com.example.chatapp.authentication

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.data.LoginState
import com.example.chatapp.data.UiState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = Firebase.auth
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun setEmail(value: String) {
        _loginState.update { currentState ->
            currentState.copy(
                email = value
            )
        }
    }

    fun setPassword(value: String) {
        _loginState.update { currentState ->
            currentState.copy(
                password = value
            )
        }
    }

    fun setLoadingState(value: Boolean) {
        _loginState.update { currentState ->
            currentState.copy(
                loading = value
            )
        }
    }

    fun login(startScreen: () -> Unit) {
        val email: String = loginState.value.email
        val password: String = loginState.value.password
        setLoadingState(true)

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    setLoadingState(false)
                    startScreen()
                } else {
                    setEmail("")
                    setPassword("")
                    setLoadingState(false)
                }
            }
    }

    fun signUp(startScreen: () -> Unit) {
        val email: String = loginState.value.email
        val password: String = loginState.value.password
        setLoadingState(true)

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    setLoadingState(false)
                    startScreen()
                } else {
                    setEmail("")
                    setPassword("")
                    setLoadingState(false)
                }
            }
    }
}