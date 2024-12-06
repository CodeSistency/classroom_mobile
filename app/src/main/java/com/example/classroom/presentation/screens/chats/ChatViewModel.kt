package com.example.classroom.presentation.screens.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.data.repository.ChatRepositoryImpl
import com.example.classroom.domain.model.entity.LocalMessages
import com.example.classroom.domain.services.ChatWebSocket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepositoryImpl
) : ViewModel() {
    private val _messages = MutableStateFlow<List<LocalMessages>>(emptyList())
    val messages: StateFlow<List<LocalMessages>> = _messages

    private val _typingUsers = MutableStateFlow<Set<Int>>(emptySet()) // Tracks users typing
    val typingUsers: StateFlow<Set<Int>> = _typingUsers

    private var chatWebSocket: ChatWebSocket? = null

    // Connect WebSocket to the server
    fun connectWebSocket(userId: Int, chatRoomId: Int) {
        chatWebSocket = ChatWebSocket(
            userId,
            chatRoomId,
            onMessageReceived = { message ->
                saveMessage(message) // Save message to local DB
            },
            onTypingStatusChanged = { typingUserId, isTyping ->
                _typingUsers.value = if (isTyping) {
                    _typingUsers.value + typingUserId
                } else {
                    _typingUsers.value - typingUserId
                }
            }
        )
        chatWebSocket?.connect()
    }

    // Send message through WebSocket
    fun sendMessage(message: String) {
        chatWebSocket?.sendMessage(message)
    }

    // Notify typing status to the server
    fun sendTypingStatus(isTyping: Boolean) {
        chatWebSocket?.sendTypingStatus(isTyping)
    }

    // Fetch messages for a chat room from local database
    fun getMessages(chatRoomId: Int) {
        viewModelScope.launch {
            _messages.value = chatRepository.getMessagesByChatRoom(chatRoomId)
        }
    }

    // Save message to local database
    fun saveMessage(message: LocalMessages) {
        viewModelScope.launch {
            chatRepository.saveMessage(message)
            // Update local messages list in state
            _messages.value = _messages.value + message
        }
    }

    // Clear all messages for a specific chat room
    fun clearMessages(chatRoomId: Int) {
        viewModelScope.launch {
            chatRepository.clearMessagesForChatRoom(chatRoomId)
        }
    }

    // Disconnect WebSocket when ViewModel is cleared
    override fun onCleared() {
        super.onCleared()
        chatWebSocket?.disconnect()
    }
}
