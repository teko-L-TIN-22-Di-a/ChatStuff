package com.example.chatapp

import androidx.lifecycle.ViewModel
import com.example.chatapp.data.UiState
import com.example.chatapp.models.Friend
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChatViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()


    fun setDarkMode(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                darkMode = value
            )
        }
    }

    fun setCurrentConversation(value: Friend) {
        _uiState.update { currentState ->
            currentState.copy(
                currentConversationContact = value
            )
        }
    }
}