package com.example.chatapp.data

import com.example.chatapp.Contact

data class UiState (
    val darkMode: Boolean = false,
    val currentConversationContact: Contact = Contact(0,"","",0)
)