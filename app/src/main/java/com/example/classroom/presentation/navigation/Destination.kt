package com.example.classroom.presentation.navigation


import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.classroom.R


/** Provee las destinaciones para la navegacion de forma type-safe */
sealed class Icon {
    data class Resource(@DrawableRes val resId: Int) : Icon()
    data class Vector(val imageVector: ImageVector) : Icon()
}
enum class Destination(val screenRoute: String, val title: String, val icon: Icon) {
    HOME(
        screenRoute = "HOME",
        title = "HOME",
        icon = Icon.Resource(R.drawable.ic_home)
    ),
    LOGIN(
        screenRoute = "LOGIN",
        title = "LOGIN",
        icon = Icon.Resource(R.drawable.ic_launcher_background)
    ),
    SPLASH(
        screenRoute = "SPLASH",
        title = "SPLASH",
        icon = Icon.Resource(R.drawable.ic_launcher_background)
    ),
    REGISTRO(
        screenRoute = "REGISTRO",
        title = "REGISTRO",
        icon = Icon.Resource(R.drawable.ic_launcher_background)
    ),
    REGISTRO_COURSE(
        screenRoute = "REGISTRO_COURSE",
        title = "REGISTRO CURSO",
        icon = Icon.Resource(R.drawable.ic_launcher_background)
    ),
    REGISTRO_ACTIVITY(
        screenRoute = "REGISTRO_ACTIVITY",
        title = "REGISTRO ACTIVIDAD",
        icon = Icon.Resource(R.drawable.ic_launcher_background)
    ),
    ACTIVITIES(
        screenRoute = "ACTIVITIES",
        title = "ACTIVITIES",
        icon = Icon.Resource(R.drawable.ic_edit)
    ),
    USER_ACTIVITIES(
        screenRoute = "USER_ACTIVITIES",
        title = "USER_ACTIVITIES",
        icon = Icon.Resource(R.drawable.ic_edit)
    ),
    COURSES(
        screenRoute = "COURSES",
        title = "COURSES",
        icon = Icon.Resource(R.drawable.ic_edit)
    ),
    ADD_USER_COURSE(
        screenRoute = "ADD_USER_COURSE",
        title = "ADD_USER_COURSE",
        icon = Icon.Resource(R.drawable.ic_launcher_background)
    ),
}