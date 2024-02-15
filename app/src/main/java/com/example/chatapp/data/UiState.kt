package com.example.chatapp.data

import com.example.chatapp.Contact
import com.example.chatapp.Friend

data class UiState (
    val darkMode: Boolean = false,
    val currentConversationContact: Friend = Friend("","",""),
    val contactList: List<Contact> = listOf()
)