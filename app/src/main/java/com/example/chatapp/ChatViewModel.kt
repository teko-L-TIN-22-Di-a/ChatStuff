package com.example.chatapp

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chatapp.data.UiState
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

    fun setCurrentConversation(value: Contact) {
        _uiState.update { currentState ->
            currentState.copy(
                currentConversationContact = value
            )
        }
    }
}