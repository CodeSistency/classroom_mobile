package com.example.classroom

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log

import com.example.classroom.di.AppModule
import com.example.classroom.di.AppModuleImpl

class App: Application() {
        companion object {
            const val NOTIFICATION_CHANNEL_ID = "notification_fcm"
            lateinit var appModule: AppModule
            lateinit var instance: App private set
            var isUpdateAvailable: Boolean? = false
            var isUserLogged: Boolean? = false
            var isDarkTheme: Boolean? = false
        }

    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "example",
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.description = "esto es una prueba"
            val notificationChannel = getSystemService(NotificationManager::class.java)
            notificationChannel.createNotificationChannel(channel)
        }
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        appModule = AppModuleImpl(this)
        Log.e("onCreateMsg", "onCreateMsg")
//        createNotificationChannel()
    }

}