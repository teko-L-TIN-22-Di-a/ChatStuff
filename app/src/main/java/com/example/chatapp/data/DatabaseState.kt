package com.example.chatapp.data

import com.example.chatapp.models.Conversation
import com.example.chatapp.models.Friend
import com.example.chatapp.models.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

data class DatabaseState(
    val auth: FirebaseAuth = Firebase.auth,
    val friendList: List<Friend> = listOf(),
    val conversationList: List<Conversation> = listOf(),
    val conversation: Conversation = Conversation(listOf(), listOf()),
    val users: List<User> = listOf()
)