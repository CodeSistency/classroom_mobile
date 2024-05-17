package com.example.classroom.presentation.screens.home.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TopBarHome(){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {

        }) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = null)
        }
        IconButton(onClick = {

        }) {
            Icon(imageVector = Icons.Default.Notifications, contentDescription = null)
        }
    }
}