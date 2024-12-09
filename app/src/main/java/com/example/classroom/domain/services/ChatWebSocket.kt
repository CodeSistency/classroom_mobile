package com.example.classroom.domain.services

import android.util.Log
import com.example.classroom.domain.model.entity.LocalMessages
import com.example.classroom.presentation.screens.chats.states.Message
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
class ChatWebSocket(
    private val userId: Int,
    private val chatRoomId: Int?,
    private val onMessageReceived: (LocalMessages) -> Unit,
    private val onTypingStatusChanged: (Int, Boolean) -> Unit, // userId, isTyping
    private val onSeenStatusChanged: (Int, Boolean) -> Unit,  // userId, isSeen
    private val onUserActivityChanged: (Int, Boolean) -> Unit // userId, isActive
) {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    // Connect to WebSocket
    fun connect() {
        val request = Request.Builder()
            .url("ws://your-backend-url/chat")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                val payload = if (chatRoomId != null) {
                    """
                    {
                        "event": "join",
                        "userId": $userId,
                        "chatRoomId": $chatRoomId
                    }
                    """.trimIndent()
                } else {
                    """
                    {
                        "event": "userConnected",
                        "userId": $userId
                    }
                    """.trimIndent()
                }
                webSocket.send(payload)
            }

            // Handle incoming messages and other events
            override fun onMessage(webSocket: WebSocket, text: String) {
                val json = JSONObject(text)
                when (json.getString("event")) {
                    "message" -> {
                        val message = Gson().fromJson(json.getString("data"), LocalMessages::class.java)
                        onMessageReceived(message)
                    }
                    "typing" -> {
                        val typingUserId = json.getInt("userId")
                        val isTyping = json.getBoolean("isTyping")
                        onTypingStatusChanged(typingUserId, isTyping)
                    }
                    "seen" -> {
                        val seenUserId = json.getInt("userId")
                        val isSeen = json.getBoolean("isSeen")
                        onSeenStatusChanged(seenUserId, isSeen)
                    }
                    "active" -> {
                        val activeUserId = json.getInt("userId")
                        val isActive = json.getBoolean("isActive")
                        onUserActivityChanged(activeUserId, isActive)
                    }
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Connection failed: ${t.message}")
            }
        })
    }

    // Send a message
    fun sendMessage(message: String, messageType: String = "TEXT", fileUrl: String? = null) {
        val payload = """
            {
                "event": "message",
                "data": {
                    "chatRoomId": $chatRoomId,
                    "senderId": $userId,
                    "content": "$message",
                    "messageType": "$messageType",
                    "fileUrl": "$fileUrl"
                }
            }
        """.trimIndent()
        webSocket?.send(payload)
    }

    // Send typing status
    fun sendTypingStatus(isTyping: Boolean) {
        val payload = """
            {
                "event": "typing",
                "userId": $userId,
                "chatRoomId": $chatRoomId,
                "isTyping": $isTyping
            }
        """.trimIndent()
        webSocket?.send(payload)
    }

    // Send seen status
    fun sendSeenStatus(isSeen: Boolean) {
        val payload = """
            {
                "event": "seen",
                "userId": $userId,
                "chatRoomId": $chatRoomId,
                "isSeen": $isSeen
            }
        """.trimIndent()
        webSocket?.send(payload)
    }

    // Send user activity status
    fun sendUserActivity(isActive: Boolean) {
        val payload = """
            {
                "event": "active",
                "userId": $userId,
                "isActive": $isActive
            }
        """.trimIndent()
        webSocket?.send(payload)
    }

    // Disconnect from WebSocket
    fun disconnect() {
        webSocket?.close(1000, "User disconnected")
    }
}

