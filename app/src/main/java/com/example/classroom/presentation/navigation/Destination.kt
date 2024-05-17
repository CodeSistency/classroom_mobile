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
        icon = Icon.Vector(Icons.Default.AccountCircle)
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
}