package com.example.classroom.presentation.screens.chats

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.classroom.domain.model.entity.LocalChatRoom

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ChatListScreen(
    viewModel: ChatViewModel,
    currentUserId: Int,
    onChatSelected: (LocalChatRoom) -> Unit,
    onCreateGroupChat: () -> Unit
) {
    val chatRooms by viewModel.chatRooms.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchChatRooms(currentUserId)
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateGroupChat) {
                Icon(Icons.Default.GroupAdd, contentDescription = "Create Group")
            }
        }
    ) {
        if (chatRooms.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No chats available.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(chatRooms) { chatRoom ->
                    ListItem(
                        text = { Text(chatRoom.name ?: "Private Chat") },
                        secondaryText = { Text(chatRoom.lastMessage ?: "No messages yet") },
                        modifier = Modifier.clickable { onChatSelected(chatRoom) }
                    )
                }
            }
        }
    }
}

