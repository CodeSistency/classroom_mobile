package com.example.classroom.presentation.screens.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classroom.data.remote.dto.chat.ChatRoomDTO
import com.example.classroom.data.remote.dto.chat.MessageDTO
import com.example.classroom.data.repository.ChatRepositoryImpl
import com.example.classroom.data.repository.RepositoryBundle
import com.example.classroom.domain.model.entity.LocalChatRoom
import com.example.classroom.domain.model.entity.LocalMessages
import com.example.classroom.domain.model.entity.LocalStudents
import com.example.classroom.domain.repository.ChatRepository
import com.example.classroom.domain.services.ChatWebSocket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class ChatViewModel @Inject constructor(
    private val repositoryBundle: RepositoryBundle
) : ViewModel() {

    private val _messages = MutableStateFlow<List<LocalMessages>>(emptyList())
    val messages: StateFlow<List<LocalMessages>> = _messages

    private val _typingUsers = MutableStateFlow<Set<Int>>(emptySet()) // Tracks users typing
    val typingUsers: StateFlow<Set<Int>> = _typingUsers

    private val _chatRooms = MutableStateFlow<List<LocalChatRoom>>(emptyList())
    val chatRooms: StateFlow<List<LocalChatRoom>> = _chatRooms

    private var webSocket: ChatWebSocket? = null

    // Fetch chat rooms
    fun fetchChatRooms(currentUserId: Int) {
        viewModelScope.launch {
            // Fetch chat rooms
            val rooms = repositoryBundle.chatRepository.getChatRooms(currentUserId)

            // For each room, fetch the last message and update the room
            val roomsWithLastMessage = rooms.map { chatRoom ->
                val lastMessage = repositoryBundle.chatRepository.getLastMessageForChat(chatRoom.id)
                chatRoom.copy(lastMessage = lastMessage?.content) // Add last message to the chat room
            }

            // Update _chatRooms with the rooms that include the last message
            _chatRooms.value = roomsWithLastMessage
        }
    }

    // Create or get a private chat room
    fun createOrGetPrivateChat(userId: Int, receiverId: Int, onRoomCreated: (LocalChatRoom) -> Unit) {
        viewModelScope.launch {
            val roomResponse = repositoryBundle.chatRepository.getOrCreatePrivateRoom(userId, receiverId)
            roomResponse.responseData?.let { room ->
                val localRoom = convertChatRoomDtoToLocal(room)
                onRoomCreated(localRoom)
                connectWebSocket(userId, localRoom.id)
            }
        }
    }

    // Fetch messages for a specific chat room
    fun fetchMessages(chatRoomId: Int) {
        viewModelScope.launch {
            val messagesResponse = repositoryBundle.chatRepository.getMessagesRemote(chatRoomId)
            messagesResponse.responseData?.let { messagesDto ->
                val localMessages = messagesDto.map { convertMessageDtoToLocal(it) }
                _messages.value = localMessages
            }
        }
    }

    // Send message and update state
    fun sendMessage(chatRoomId: Int, userId: Int, content: String, messageType: String = "TEXT", fileUrl: String? = null) {
        viewModelScope.launch {

            //It lacks the file functionality
            val newMessageResponse = repositoryBundle.chatRepository.sendMessage(chatRoomId, userId, content, messageType)

//            val newMessageResponse = repositoryBundle.chatRepository.sendMessage(chatRoomId, userId, content, messageType, fileUrl)
            newMessageResponse.responseData?.let { newMessageDto ->
                val newMessage = convertMessageDtoToLocal(newMessageDto)
                _messages.value = _messages.value + newMessage
                webSocket?.sendMessage(newMessage.content ?: "", newMessage.messageType, newMessage.fileUrl)
            }
        }
    }

    // Set typing status for the current user
    fun setTypingStatus(chatRoomId: Int, userId: Int, isTyping: Boolean) {
        viewModelScope.launch {
            webSocket?.sendTypingStatus(isTyping)
        }
    }

    // Connect to WebSocket and handle events
    fun connectWebSocket(userId: Int, chatRoomId: Int) {
        webSocket = ChatWebSocket(
            userId = userId,
            chatRoomId = chatRoomId,
            onMessageReceived = { newMessage ->
                viewModelScope.launch {


                    _messages.value = _messages.value + newMessage
                }
            },
            onTypingStatusChanged = { typingUserId, isTyping ->
                _typingUsers.value = if (isTyping) {
                    _typingUsers.value + typingUserId
                } else {
                    _typingUsers.value - typingUserId
                }
            },
            onSeenStatusChanged = { seenUserId, isSeen ->
                // Handle seen status if needed, such as updating the UI with message read status
            },
            onUserActivityChanged = { activeUserId, isActive ->
                // Handle user activity if needed
            }
        ).apply { connect() }
    }

    // Disconnect WebSocket
    fun disconnectWebSocket() {
        webSocket?.disconnect()
    }

    fun convertChatRoomDtoToLocal(chatRoomDto: ChatRoomDTO): LocalChatRoom {
        return LocalChatRoom(
            id = chatRoomDto.id,
            name = chatRoomDto.name,
            createdAt = chatRoomDto.createdAt,
            isGroup = chatRoomDto.isGroup
//            updatedAt = chatRoomDto.updatedAt
        )
    }

    fun convertMessageDtoToLocal(messageDto: MessageDTO): LocalMessages {
        return LocalMessages(
            id = messageDto.id,
            chatRoomId = messageDto.chatRoomId,
            senderId = messageDto.senderId,
            content = messageDto.content,
            messageType = messageDto.messageType,
            fileUrl = messageDto.fileUrl,
            isRead = messageDto.isRead,
            sentAt = messageDto.sentAt
//            createdAt = messageDto.createdAt
        )
    }

    private val _groupName = MutableStateFlow("")
    val groupName: StateFlow<String> = _groupName

    private val _users = MutableStateFlow<List<LocalStudents>>(emptyList())
    val users: StateFlow<List<LocalStudents>> = _users

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun onGroupNameChange(newName: String) {
        _groupName.value = newName
    }

    fun fetchUsers(courseId: Int) {
        viewModelScope.launch {
//            _isLoading.value = true
//            _errorMessage.value = null
//            try {
//                val fetchedUsers = userRepository.getUsersByCourse(courseId)
//                _users.value = fetchedUsers
//            } catch (e: Exception) {
//                _errorMessage.value = e.localizedMessage ?: "Failed to fetch users"
//            } finally {
//                _isLoading.value = false
//            }
        }
    }
}
