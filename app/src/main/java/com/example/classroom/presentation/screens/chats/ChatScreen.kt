package com.example.classroom.presentation.screens.chats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.classroom.domain.model.entity.LocalChatRoom
import com.example.classroom.presentation.screens.chats.composable.MessageBubble
import com.example.classroom.presentation.screens.chats.composable.TypingIndicator

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    receiverId: Int?,  // For one-on-one chats
    chatRoomId: Int?,  // For group chats
    userId: Int
) {
    val messages by viewModel.messages.collectAsState()
    val (messageText, setMessageText) = remember { mutableStateOf("") }
    val isTyping = remember { mutableStateOf(false) }

    LaunchedEffect(chatRoomId, receiverId) {
        if (chatRoomId == null && receiverId != null) {
            viewModel.createOrGetPrivateChat(userId, receiverId) { room ->
                viewModel.fetchMessages(room.id)
            }
        } else if (chatRoomId != null) {
            viewModel.connectWebSocket(userId, chatRoomId)
            viewModel.fetchMessages(chatRoomId)
        }
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.disconnectWebSocket() }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(messages) { message ->
                MessageBubble(message = message, isMine = message.senderId == userId)
            }
        }

        Row(modifier = Modifier.padding(8.dp)) {
            TextField(
                value = messageText,
                onValueChange = {
                    setMessageText(it)
                    if (!isTyping.value) {
                        isTyping.value = true
                        viewModel.setTypingStatus(chatRoomId ?: 0, userId, true)
                    }
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") }
            )
            IconButton(onClick = {
                viewModel.sendMessage(chatRoomId ?: 0, userId, messageText)
                setMessageText("")
                isTyping.value = false
                viewModel.setTypingStatus(chatRoomId ?: 0, userId, false)
            }) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}
