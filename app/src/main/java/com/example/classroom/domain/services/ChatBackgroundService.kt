package com.example.classroom.domain.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.classroom.data.local.db.daos.MessageDao
import com.example.classroom.domain.model.entity.LocalChatRoom
import com.example.classroom.domain.model.entity.LocalMessages
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject


class ChatBackgroundService(
    private val chatDao: MessageDao,
    private val webSocket: ChatWebSocket
) : Service() {

    private val binder = ChatBinder()
    private var isServiceRunning = false

    // WebSocket listener for real-time updates
    private val webSocketListener = object : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            // Send an initial message or join room here if needed
            Log.d("WebSocket", "Connected")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            val json = JSONObject(text)
            when (json.getString("event")) {
                "message" -> {
                    val message = Gson().fromJson(json.getString("data"), LocalMessages::class.java)
                    // Insert message into the database
                    GlobalScope.launch {
                        updateMessageInDb(message)

                    }
                }
                "chatRoom" -> {
                    val chatRoom = Gson().fromJson(json.getString("data"), LocalChatRoom::class.java)
                    // Insert new chat room into the database
                    GlobalScope.launch {
                        insertChatRoomInDb(chatRoom)

                    }
                }
                "typing" -> {
                    // Handle typing status (not in database, just for UI update)
                    val userId = json.getInt("userId")
                    val isTyping = json.getBoolean("isTyping")
                    // Update UI or notify activity/fragment with this info
                }
                "seen" -> {
                    // Handle seen status (not in database, just for UI update)
                    val userId = json.getInt("userId")
                    val isSeen = json.getBoolean("isSeen")
                    // Update UI or notify activity/fragment with this info
                }
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.e("WebSocket", "Connection failed: ${t.message}")
        }
    }

    // Method to update message in the local DB
    private suspend fun updateMessageInDb(message: LocalMessages) {
        chatDao.insertMessage(message)
        // Here you can also update the message read status if needed
    }

    // Method to insert new chat room into the local DB
    private suspend fun insertChatRoomInDb(chatRoom: LocalChatRoom) {
        chatDao.insertChatRoom(chatRoom)
        // Optionally, insert related users for this chat room
    }

    // Service lifecycle methods
    override fun onCreate() {
        super.onCreate()
        isServiceRunning = true
        webSocket.connect() // Start listening for updates

        // Start listening in background
        startListeningForUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
        webSocket.disconnect() // Disconnect WebSocket when service is destroyed
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    private fun startListeningForUpdates() {
        // This method could handle any other background updates if needed
    }

    // Binder to interact with the service from other components
    inner class ChatBinder : Binder() {
        fun getService(): ChatBackgroundService = this@ChatBackgroundService
    }
}
