package com.example.classroom

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface

import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.presentation.navigation.Navigation
import com.example.classroom.presentation.theme.ClassroomTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.messaging.FirebaseMessaging
import android.Manifest
import android.content.Intent
import com.example.classroom.common.firebase.saveTokenToPreferences
import com.example.classroom.domain.services.ChatBackgroundService


class MainActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        Seeders(lifecycle).seedDatabase(App.appModule.db.appDao, App.appModule.db.quizDao)
        requestNotificationPermission(this)

        setContent {
            val systemUiController = rememberSystemUiController()
            val isUserLogged by produceState<List<LocalUser?>?>(initialValue = null, producer = {
                value = App.appModule.db.appDao.getUserInfo()
            })

            ClassroomTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    isUserLogged?.let {user ->
                        Navigation(
                            isUserLogged = user.isNotEmpty(),
                            darkTheme = true,

                        ){
                        }
                    }
                }
            }
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                saveTokenToPreferences(this, token = task.result)
                Log.e("FCM", "Firebase Token: ${task.result}")
            }
        }

        val serviceIntent = Intent(this, ChatBackgroundService::class.java)
        startService(serviceIntent)
    }
    override fun onDestroy() {
        super.onDestroy()
        // Stop the service when activity is destroyed
        val serviceIntent = Intent(this, ChatBackgroundService::class.java)
        stopService(serviceIntent)
    }
}

fun requestNotificationPermission(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                (context as Activity),
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001 // Request code
            )
        }
    }


}



