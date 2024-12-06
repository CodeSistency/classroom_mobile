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
    private val senderId: Int,
    private val receiverId: Int,
    private val chatRoomId: Int,
    private val onMessageReceived: (LocalMessages) -> Unit,
    private val onTypingStatusChanged: (Int, Boolean) -> Unit // userId, isTyping
) {
    private val client = OkHttpClient()
    private var webSocket: WebSocket? = null

    fun connect() {
        val request = Request.Builder()
            .url("ws://your-backend-url/chat")
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                val payload = """
                    {
                        "userId": $userId,
                        "chatRoomId": $chatRoomId
                    }
                """.trimIndent()
                webSocket.send(payload)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val json = JSONObject(text)
                when (json.getString("event")) {
                    "message" -> {
                        val message = Gson().fromJson(json.getString("data"), Message::class.java)
                        val messageEntity = LocalMessages(
                            content = message.content,
                            senderId = message.senderId,
                            chatRoomId = message.chatRoomId,
                            createdAt = message.createdAt
                        )
                        onMessageReceived(messageEntity) // Save to local DB
                    }
                    "typing" -> {
                        val typingUserId = json.getInt("userId")
                        val isTyping = json.getBoolean("isTyping")
                        onTypingStatusChanged(typingUserId, isTyping)
                    }
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Connection failed: ${t.message}")
            }
        })
    }

    fun sendMessage(message: String) {
        val payload = """
            {
                "event": "message",
                "data": {
                    "chatRoomId": $chatRoomId,
                    "senderId": $userId,
                    "message": "$message"
                }
            }
        """.trimIndent()
        webSocket?.send(payload)
    }

    fun sendTypingStatus(isTyping: Boolean) {
        val payload = """
            {
                "event": "typing",
                "chatRoomId": $chatRoomId,
                "userId": $userId,
                "isTyping": $isTyping
            }
        """.trimIndent()
        webSocket?.send(payload)
    }

    fun disconnect() {
        webSocket?.close(1000, "User disconnected")
    }
}