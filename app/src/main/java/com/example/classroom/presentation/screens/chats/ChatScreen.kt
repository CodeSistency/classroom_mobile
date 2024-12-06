package com.example.classroom.presentation.screens.chats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.classroom.presentation.screens.chats.composable.MessageBubble
import com.example.classroom.presentation.screens.chats.composable.TypingIndicator

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    userId: Int,
    chatRoomId: Int
) {
    val messages by viewModel.messages.collectAsState()
    val typingUsers by viewModel.typingUsers.collectAsState()
    val (messageText, setMessageText) = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.connectWebSocket(userId, chatRoomId)
    }

    LaunchedEffect(Unit) {
        viewModel.getMessages(chatRoomId)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            reverseLayout = true // Reverse the order to display messages bottom-up
        ) {
            items(messages) { message ->
                MessageBubble(
                    message = message,
                    isMine = message.senderId == userId // Check if it's the current user's message
                )
            }
        }

        // Show typing indicator
        TypingIndicator(typingUsers = typingUsers)

        Row(modifier = Modifier.padding(8.dp)) {
            TextField(
                value = messageText,
                onValueChange = {
                    setMessageText(it)
                    viewModel.sendTypingStatus(it.isNotEmpty())
                },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") }
            )
            Button(
                onClick = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendMessage(messageText)
                        setMessageText("") // Clear message input
                        viewModel.sendTypingStatus(false) // Stop typing notification
                    }
                }
            ) {
                Text("Send")
            }
        }
    }
}