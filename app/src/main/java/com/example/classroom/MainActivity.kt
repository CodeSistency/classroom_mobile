package com.example.classroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.coroutineScope
import androidx.room.Room
import com.example.classroom.common.Seeders
import com.example.classroom.di.AppModuleImpl
import com.example.classroom.domain.model.entity.LocalUser
import com.example.classroom.presentation.navigation.Navigation
import com.example.classroom.presentation.theme.ClassroomTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch
import proyecto.person.appconsultapopular.data.local.db.AppDatabase

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).build()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Seeders(lifecycle).addCourses(db)
        setContent {
            val systemUiController = rememberSystemUiController()
//            systemUiController.setStatusBarColor(
//                color = if (isDarkTheme) Gris else Gris
//            )
//            systemUiController.setNavigationBarColor(
//                color = if (isDarkTheme) darkColor else Color.White
//            )
//            LaunchedEffect(key1 = true, block = {
//                db.appDao.insertLocalUser(
//                    LocalUser(
//                        email = "",
//                        userName = "",
//                        userId = "",
//
//                        )
//                )
//            })

            val isUserLogged by produceState<List<LocalUser?>?>(initialValue = null, producer = {
                value = db.appDao.getUserInfo()
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
    }
}



