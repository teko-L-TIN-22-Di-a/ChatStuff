package com.example.chatapp.data

import com.example.chatapp.ContactList
import com.example.chatapp.Friend
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage

data class DatabaseState(
    val auth: FirebaseAuth = Firebase.auth,
    val friendList: List<Friend> = listOf()

)