package com.example.classroom.presentation.screens.chats

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateGroupChatScreen(
    courseId: Int, // Course ID to fetch users
    viewModel: ChatViewModel,
    onCreateGroupChat: (String, List<Int>) -> Unit, // Callback with group name and selected user IDs
    onCancel: () -> Unit // Callback to navigate back or cancel
) {
    val groupName by viewModel.groupName.collectAsState()
    val selectedUsers = remember { mutableStateListOf<Int>() }
    val users by viewModel.users.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(courseId) {
        viewModel.fetchUsers(courseId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Group Chat") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(errorMessage ?: "An error occurred")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                TextField(
                    value = groupName,
                    onValueChange = viewModel::onGroupNameChange,
                    label = { Text("Group Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Add Participants", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn {
                    items(users) { user ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (selectedUsers.contains(user.id)) {
                                        selectedUsers.remove(user.id)
                                    } else {
                                        selectedUsers.add(user.id)
                                    }
                                }
                                .padding(8.dp)
                        ) {
                            Checkbox(
                                checked = selectedUsers.contains(user.id),
                                onCheckedChange = null // Handled by Row's clickable
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(user.name)
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        if (groupName.isNotBlank() && selectedUsers.isNotEmpty()) {
                            onCreateGroupChat(groupName, selectedUsers)
                        } else {
                            // Show error or validation message if needed
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create Group")
                }
            }
        }
    }
}
