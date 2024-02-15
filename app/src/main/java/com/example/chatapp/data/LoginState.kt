package com.example.chatapp.data

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

data class LoginState (
    val auth: FirebaseAuth = Firebase.auth,
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false
)