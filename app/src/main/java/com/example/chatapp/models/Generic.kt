package com.example.chatapp.models

import com.google.firebase.Timestamp

//data class Contact(val uid: Int, val name: String, val excerpt: String, val profilePicture: Int)
data class Message(val uid: String = "", val timestamp: Timestamp = Timestamp.now(), val text: String = "")
data class Friend(val email: String = "", val name: String = "", val uid: String = "")
data class User(val friends: List<Friend> = listOf(), val uid: String = "")
data class Contact(val user: User)
data class Conversation(
    val messages: List<Message>? = listOf(),
    val users: List<String>? = listOf()
)